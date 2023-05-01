package com.gempukku.libgdx.ui.undo;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class UndoableValidatableTextField extends VisValidatableTextField {
    private String oldText;
    private boolean fireOnInvalidInput = false;

    public UndoableValidatableTextField() {
        this("");
    }

    public UndoableValidatableTextField(String text) {
        this(text, "default");
    }

    public UndoableValidatableTextField(String text, String styleName) {
        this(text, VisUI.getSkin().get(styleName, VisTextFieldStyle.class));
    }

    public UndoableValidatableTextField(InputValidator validator) {
        this();
        addValidator(validator);
    }

    public UndoableValidatableTextField(InputValidator... validators) {
        this();
        for (InputValidator validator : validators) {
            addValidator(validator);
        }
    }

    public UndoableValidatableTextField(boolean restoreLastValid, InputValidator validator) {
        this(validator);
        setRestoreLastValid(restoreLastValid);
    }

    public UndoableValidatableTextField(boolean restoreLastValid, InputValidator... validators) {
        this(validators);
        setRestoreLastValid(restoreLastValid);
    }

    public UndoableValidatableTextField(String text, VisTextFieldStyle style) {
        super(text, style);
        oldText = text;
    }

    public boolean isFireOnInvalidInput() {
        return fireOnInvalidInput;
    }

    public void setFireOnInvalidInput(boolean fireOnInvalidInput) {
        this.fireOnInvalidInput = fireOnInvalidInput;
    }

    @Override
    public boolean fire(Event event) {
        if (event instanceof ChangeListener.ChangeEvent) {
            if (fireOnInvalidInput || isInputValid()) {
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
                return false;
            }
        } else {
            return super.fire(event);
        }
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
}
