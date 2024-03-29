package com.gempukku.libgdx.graph.data;

public interface Graph {
    String getType();

    GraphNode getNodeById(String nodeId);

    Iterable<? extends GraphNode> getNodes();

    Iterable<? extends GraphConnection> getConnections();

    Iterable<? extends NodeGroup> getGroups();
}
