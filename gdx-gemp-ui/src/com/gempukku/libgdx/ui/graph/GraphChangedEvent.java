package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.gempukku.libgdx.undo.UndoableAction;
import com.gempukku.libgdx.undo.event.UndoableEvent;

public class GraphChangedEvent extends Event implements UndoableEvent {
    private boolean structure;
    private boolean data;
    private UndoableAction undoableAction;

    public void setStructure(boolean structure) {
        this.structure = structure;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public void setUndoableAction(UndoableAction undoableAction) {
        this.undoableAction = undoableAction;
    }

    public boolean isStructure() {
        return structure;
    }

    public boolean isData() {
        return data;
    }

    @Override
    public UndoableAction getUndoableAction() {
        return undoableAction;
    }

    @Override
    public void reset() {
        structure = false;
        data = false;
        undoableAction = null;
    }
}
