package com.gempukku.libgdx.graph.data.impl;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.NodeGroup;

public class DefaultNodeGroup implements NodeGroup {
    private String name;
    private final ObjectSet<String> nodeIds = new ObjectSet<>();

    public DefaultNodeGroup(String name, ObjectSet<String> nodeIds) {
        this.name = name;
        this.nodeIds.addAll(nodeIds);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectSet<String> getNodeIds() {
        return nodeIds;
    }
}
