package com.gempukku.libgdx.undo;

import com.badlogic.gdx.utils.Array;

public class CompositeUndoableAction implements UndoableAction {
    private final Array<UndoableAction> undoableActions = new Array<>();
    private String undoTextRepresentation;
    private String redoTextRepresentation;

    public CompositeUndoableAction() {
        this(null, null);
    }

    public CompositeUndoableAction(String undoTextRepresentation, String redoTextRepresentation) {
        this.undoTextRepresentation = undoTextRepresentation;
        this.redoTextRepresentation = redoTextRepresentation;
    }

    public void setUndoTextRepresentation(String undoTextRepresentation) {
        this.undoTextRepresentation = undoTextRepresentation;
    }

    public void setRedoTextRepresentation(String redoTextRepresentation) {
        this.redoTextRepresentation = redoTextRepresentation;
    }

    public void addUndoableAction(UndoableAction undoableAction) {
        undoableActions.add(undoableAction);
    }

    public boolean hasActions() {
        return !undoableActions.isEmpty();
    }

    @Override
    public void undoAction() {
        // Last to first
        for (int i = undoableActions.size - 1; i >= 0; i--) {
            undoableActions.get(i).undoAction();
        }
    }

    @Override
    public void redoAction() {
        // First to last
        for (int i = 0; i < undoableActions.size; i++) {
            undoableActions.get(i).redoAction();
        }
    }

    public void doAction() {
        redoAction();
    }

    @Override
    public boolean canUndo() {
        for (UndoableAction undoableAction : undoableActions) {
            if (!undoableAction.canUndo())
                return false;
        }

        return true;
    }

    @Override
    public boolean canRedo() {
        for (UndoableAction undoableAction : undoableActions) {
            if (!undoableAction.canRedo())
                return false;
        }

        return true;
    }

    @Override
    public String getUndoTextRepresentation() {
        return undoTextRepresentation;
    }

    @Override
    public String getRedoTextRepresentation() {
        return redoTextRepresentation;
    }
}
