package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;

public class GraphChangedEvent extends Event {
    private boolean structure;
    private boolean data;

    public GraphChangedEvent(boolean structure, boolean data) {
        this.structure = structure;
        this.data = data;
        System.out.println("Graph changed");
    }

    public boolean isStructure() {
        return structure;
    }

    public boolean isData() {
        return data;
    }
}
