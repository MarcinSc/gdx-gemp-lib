package com.gempukku.libgdx.undo;

public interface UndoableAction {
    void undoAction();
    void redoAction();
    boolean canUndo();
    boolean canRedo();
    String getUndoTextRepresentation();
    String getRedoTextRepresentation();
}
