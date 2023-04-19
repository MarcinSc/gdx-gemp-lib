package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class GraphChangesAggregation {
    private final Actor actor;
    private boolean aggregating = false;

    private boolean structureChanged;
    private boolean dataChanged;
    private boolean changed;

    public GraphChangesAggregation(Actor actor) {
        this.actor = actor;
    }

    public void processChange(boolean structure, boolean data) {
        if (aggregating) {
            structureChanged |= structure;
            dataChanged |= data;
            changed = true;
        } else {
            actor.fire(new GraphChangedEvent(structure, data));
        }
    }

    public void startAggregating() {
        aggregating = true;
    }

    public void finishAggregating() {
        aggregating = false;
        if (changed) {
            actor.fire(new GraphChangedEvent(structureChanged, dataChanged));
            reset();
        }
    }

    private void reset() {
        aggregating = false;
        structureChanged = false;
        dataChanged = false;
        changed = false;
    }
}
