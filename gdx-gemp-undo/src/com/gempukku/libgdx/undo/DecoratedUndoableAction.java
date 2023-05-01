package com.gempukku.libgdx.undo;

public class DecoratedUndoableAction implements UndoableAction {
    private final UndoableAction decorated;

    private UndoableAction beforeUndo;
    private UndoableAction beforeRedo;
    private UndoableAction afterUndo;
    private UndoableAction afterRedo;

    public DecoratedUndoableAction(UndoableAction decorated) {
        this.decorated = decorated;
    }

    public void setBeforeUndo(UndoableAction beforeUndo) {
        this.beforeUndo = beforeUndo;
    }

    public void setBeforeRedo(UndoableAction beforeRedo) {
        this.beforeRedo = beforeRedo;
    }

    public void setAfterUndo(UndoableAction afterUndo) {
        this.afterUndo = afterUndo;
    }

    public void setAfterRedo(UndoableAction afterRedo) {
        this.afterRedo = afterRedo;
    }

    @Override
    public void undoAction() {
        if (beforeUndo != null)
            beforeUndo.undoAction();
        decorated.undoAction();
        if (afterUndo != null)
            afterUndo.undoAction();
    }

    @Override
    public void redoAction() {
        if (beforeRedo != null)
            beforeRedo.redoAction();
        decorated.redoAction();
        if (afterRedo != null)
            afterRedo.redoAction();;
    }

    @Override
    public boolean canUndo() {
        return decorated.canUndo();
    }

    @Override
    public boolean canRedo() {
        return decorated.canRedo();
    }

    @Override
    public String getUndoTextRepresentation() {
        return decorated.getUndoTextRepresentation();
    }

    @Override
    public String getRedoTextRepresentation() {
        return decorated.getRedoTextRepresentation();
    }
}
