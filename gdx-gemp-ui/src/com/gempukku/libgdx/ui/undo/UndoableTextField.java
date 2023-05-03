package com.gempukku.libgdx.ui.undo;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextField;

public class UndoableTextField extends VisTextField {
    private String oldText;

    public UndoableTextField() {
        this("");
    }

    public UndoableTextField(String text) {
        this(text, "default");
    }

    public UndoableTextField(String text, String styleName) {
        this(text, VisUI.getSkin().get(styleName, VisTextFieldStyle.class));
    }

    public UndoableTextField(String text, VisTextFieldStyle style) {
        super(text, style);
        this.oldText = text;
    }

    @Override
    public boolean fire(Event event) {
        if (event instanceof ChangeListener.ChangeEvent) {
            String text = getText();
            UndoableChangeEvent undoableEvent = Pools.obtain(UndoableChangeEvent.class);
            undoableEvent.setUndoableAction(new SetTextAction(oldText, text));
            boolean cancelled = super.fire(undoableEvent);
            if (!cancelled) {
                oldText = text;
            }
            Pools.free(undoableEvent);
            return cancelled;
        } else {
            return super.fire(event);
        }
    }

    @Override
    protected InputListener createInputListener() {
        return new UndoableTextFieldClickListener();
    }

    private class SetTextAction extends DefaultUndoableAction {
        private final String oldText;
        private final String newText;

        public SetTextAction(String oldText, String newText) {
            this.oldText = oldText;
            this.newText = newText;
        }

        @Override
        public void undoAction() {
            setText(oldText);
        }

        @Override
        public void redoAction() {
            setText(newText);
        }
    }

    private class UndoableTextFieldClickListener extends TextFieldClickListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            boolean ctrl = UIUtils.ctrl();
            if (ctrl && keycode == Input.Keys.Z && !isReadOnly()) {
                return false;
            }

            return super.keyDown(event, keycode);
        }
    }
}
