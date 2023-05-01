package com.gempukku.libgdx.undo;

public class DefaultUndoableAction implements UndoableAction {
    private boolean canUndo;
    private boolean canRedo;

    public DefaultUndoableAction() {
        this(true, true);
    }

    public DefaultUndoableAction(boolean canUndo, boolean canRedo) {
        this.canUndo = canUndo;
        this.canRedo = canRedo;
    }

    @Override
    public void undoAction() {

    }

    @Override
    public void redoAction() {

    }

    public void doAction() {
        redoAction();
    }

    @Override
    public boolean canUndo() {
        return canUndo;
    }

    @Override
    public boolean canRedo() {
        return canRedo;
    }

    @Override
    public String getUndoTextRepresentation() {
        return null;
    }

    @Override
    public String getRedoTextRepresentation() {
        return null;
    }
}
