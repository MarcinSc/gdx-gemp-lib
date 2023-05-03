package com.gempukku.libgdx.undo;

public class DecoratedUndoableAction implements UndoableAction {
    private final UndoableAction decorated;

    private Runnable beforeUndo;
    private Runnable beforeRedo;
    private Runnable afterUndo;
    private Runnable afterRedo;

    public DecoratedUndoableAction(UndoableAction decorated) {
        this.decorated = decorated;
    }

    public void setBeforeUndo(Runnable beforeUndo) {
        this.beforeUndo = beforeUndo;
    }

    public void setBeforeRedo(Runnable beforeRedo) {
        this.beforeRedo = beforeRedo;
    }

    public void setAfterUndo(Runnable afterUndo) {
        this.afterUndo = afterUndo;
    }

    public void setAfterRedo(Runnable afterRedo) {
        this.afterRedo = afterRedo;
    }

    @Override
    public void undoAction() {
        if (beforeUndo != null)
            beforeUndo.run();
        decorated.undoAction();
        if (afterUndo != null)
            afterUndo.run();
    }

    @Override
    public void redoAction() {
        if (beforeRedo != null)
            beforeRedo.run();
        decorated.redoAction();
        if (afterRedo != null)
            afterRedo.run();;
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
