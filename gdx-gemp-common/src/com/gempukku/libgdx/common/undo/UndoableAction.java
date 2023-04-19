package com.gempukku.libgdx.common.undo;

public interface UndoableAction {
    void undoAction();
    void redoAction();
    boolean canUndo();
    boolean canRedo();
    String getUndoTextRepresentation();
    String getRedoTextRepresentation();
}
