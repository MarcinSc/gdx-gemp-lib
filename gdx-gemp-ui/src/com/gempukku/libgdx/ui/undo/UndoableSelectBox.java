package com.gempukku.libgdx.ui.undo;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisSelectBox;

public class UndoableSelectBox<T> extends VisSelectBox<T> {
    private T oldValue;

    public UndoableSelectBox() {
        this("default");
    }

    public UndoableSelectBox(String styleName) {
        this(VisUI.getSkin().get(styleName, SelectBoxStyle.class));
    }

    public UndoableSelectBox(SelectBoxStyle style) {
        super(style);
    }

    @Override
    public boolean fire(Event event) {
        if (event instanceof ChangeListener.ChangeEvent) {
            T newValue = getSelected();
            UndoableChangeEvent undoableEvent = Pools.obtain(UndoableChangeEvent.class);
            undoableEvent.setUndoableAction(new SetValueAction(oldValue, newValue));
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

    private class SetValueAction extends DefaultUndoableAction {
        private final T oldValue;
        private final T newValue;

        public SetValueAction(T oldValue, T newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void undoAction() {
            setSelected(oldValue);
        }

        @Override
        public void redoAction() {
            setSelected(newValue);
        }
    }
}
