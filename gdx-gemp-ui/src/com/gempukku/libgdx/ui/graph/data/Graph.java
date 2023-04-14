package com.gempukku.libgdx.ui.graph.data;

public interface Graph {
    Iterable<? extends GraphNode> getNodes();

    Iterable<? extends GraphConnection> getConnections();

    Iterable<? extends NodeGroup> getGroups();
}
