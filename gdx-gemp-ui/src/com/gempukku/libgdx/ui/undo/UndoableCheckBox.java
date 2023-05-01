package com.gempukku.libgdx.ui.undo;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;

public class UndoableCheckBox extends VisCheckBox {
    private boolean oldValue;

    public UndoableCheckBox(String text) {
        this(text, false);
    }

    public UndoableCheckBox(String text, boolean checked) {
        super(text, checked);
        oldValue = checked;
    }

    public UndoableCheckBox(String text, String styleName) {
        this(text, VisUI.getSkin().get(styleName, VisCheckBoxStyle.class));
    }

    public UndoableCheckBox(String text, VisCheckBoxStyle style) {
        super(text, style);
    }

    @Override
    public boolean fire(Event event) {
        if (event instanceof ChangeListener.ChangeEvent) {
            boolean newValue = isChecked();
            UndoableChangeEvent undoableEvent = Pools.obtain(UndoableChangeEvent.class);
            undoableEvent.setUndoableAction(new SetCheckedAction(oldValue, newValue));
            boolean cancelled = super.fire(undoableEvent);
            if (!cancelled) {
                oldValue = newValue;
            }
            Pools.free(undoableEvent);
            return cancelled;
        } else {
            return super.fire(event);
        }
    }

    private class SetCheckedAction extends DefaultUndoableAction {
        private final boolean oldValue;
        private final boolean newValue;

        public SetCheckedAction(boolean oldValue, boolean newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void undoAction() {
            setChecked(oldValue);
        }

        @Override
        public void redoAction() {
            setChecked(newValue);
        }
    }
}
