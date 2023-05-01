package com.gempukku.libgdx.undo;

import com.badlogic.gdx.utils.Array;

public class DefaultUndoManager implements UndoManager {
    private final Array<UndoableAction> undoableActions = new Array<>();
    private final Array<UndoableAction> redoableActions = new Array<>();

    private final int maxUndoDepth;
    private boolean ignoreUndoableActions;

    public DefaultUndoManager() {
        this(32);
    }

    public DefaultUndoManager(int maxUndoDepth) {
        this.maxUndoDepth = maxUndoDepth;
    }

    @Override
    public void addUndoableAction(UndoableAction undoableAction) {
        if (!ignoreUndoableActions) {
            undoableActions.add(undoableAction);
            if (undoableActions.size>maxUndoDepth) {
                undoableActions.removeIndex(0);
            }
            redoableActions.clear();
        }
    }

    public String getUndoText() {
        for (int i = undoableActions.size - 1; i >= 0; i--) {
            UndoableAction undoableAction = undoableActions.get(i);
            if (undoableAction.canUndo()) {
                String undoTextRepresentation = undoableAction.getUndoTextRepresentation();
                if (undoTextRepresentation != null)
                    return undoTextRepresentation;
                else
                    return "Undo";
            }
        }
        return "Undo";
    }

    public String getRedoText() {
        for (int i = redoableActions.size - 1; i >= 0; i--) {
            UndoableAction undoableAction = redoableActions.get(i);
            if (undoableAction.canUndo()) {
                String redoTextRepresentation = undoableAction.getRedoTextRepresentation();
                if (redoTextRepresentation != null)
                    return redoTextRepresentation;
                else
                    return "Redo";
            }
        }
        return "Redo";
    }

    public boolean canUndo() {
        for (UndoableAction undoableAction : undoableActions) {
            if (undoableAction.canUndo())
                return true;
        }
        return false;
    }

    public boolean canRedo() {
        for (UndoableAction redoableAction : redoableActions) {
            if (redoableAction.canRedo())
                return true;
        }
        return false;
    }

    public void undo() {
        ignoreUndoableActions = true;
        try {
            while (undoableActions.size > 0) {
                UndoableAction undoableAction = undoableActions.pop();
                if (undoableAction.canUndo()) {
                    undoableAction.undoAction();
                    redoableActions.add(undoableAction);
                    return;
                }
            }
        } finally {
            ignoreUndoableActions = false;
        }
    }

    public void redo() {
        ignoreUndoableActions = true;
        try {
            while (redoableActions.size > 0) {
                UndoableAction undoableAction = redoableActions.pop();
                if (undoableAction.canRedo()) {
                    undoableAction.redoAction();
                    undoableActions.add(undoableAction);
                    return;
                }
            }
        } finally {
            ignoreUndoableActions = false;
        }
    }
}
