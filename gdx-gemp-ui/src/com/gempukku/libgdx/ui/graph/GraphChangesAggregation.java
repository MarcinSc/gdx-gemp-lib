package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.CompositeUndoableAction;
import com.gempukku.libgdx.undo.UndoableAction;

public class GraphChangesAggregation {
    private final Actor actor;
    private boolean aggregating = false;

    private boolean structureChanged;
    private boolean dataChanged;
    private boolean changed;
    private CompositeUndoableAction compositeUndoableAction = new CompositeUndoableAction();

    public GraphChangesAggregation(Actor actor) {
        this.actor = actor;
    }

    public void processChange(boolean structure, boolean data, UndoableAction undoableAction) {
        if (aggregating) {
            structureChanged |= structure;
            dataChanged |= data;
            if (undoableAction != null)
                compositeUndoableAction.addUndoableAction(undoableAction);
            changed = true;
        } else {
            GraphChangedEvent event = Pools.obtain(GraphChangedEvent.class);
            event.setStructure(structure);
            event.setData(data);
            event.setUndoableAction(undoableAction);
            actor.fire(event);
            Pools.free(event);
        }
    }

    public void startAggregating() {
        aggregating = true;
    }

    public void finishAggregating() {
        aggregating = false;
        if (changed) {
            if (compositeUndoableAction.hasActions()) {
                GraphChangedEvent event = Pools.obtain(GraphChangedEvent.class);
                event.setStructure(structureChanged);
                event.setData(dataChanged);
                event.setUndoableAction(compositeUndoableAction);
                actor.fire(event);
                Pools.free(event);
            } else {
                GraphChangedEvent event = Pools.obtain(GraphChangedEvent.class);
                event.setStructure(structureChanged);
                event.setData(dataChanged);
                actor.fire(event);
                Pools.free(event);
            }
            reset();
        }
    }

    private void reset() {
        aggregating = false;
        structureChanged = false;
        dataChanged = false;
        changed = false;
        compositeUndoableAction = new CompositeUndoableAction();
    }
}
