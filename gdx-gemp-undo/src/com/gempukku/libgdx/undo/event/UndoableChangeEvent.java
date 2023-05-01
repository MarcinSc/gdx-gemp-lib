package com.gempukku.libgdx.undo.event;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.undo.UndoableAction;

public class UndoableChangeEvent extends ChangeListener.ChangeEvent implements UndoableEvent {
    private UndoableAction undoableAction;

    public void setUndoableAction(UndoableAction undoableAction) {
        this.undoableAction = undoableAction;
    }

    public UndoableAction getUndoableAction() {
        return undoableAction;
    }

    @Override
    public void reset() {
        super.reset();
        undoableAction = null;
    }
}
