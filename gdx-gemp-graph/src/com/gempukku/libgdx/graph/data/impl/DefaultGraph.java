package com.gempukku.libgdx.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.NodeGroup;

public class DefaultGraph<T extends GraphNode, U extends GraphConnection, V extends NodeGroup> implements Graph {
    private String type;
    private ObjectMap<String, T> graphNodes = new ObjectMap<>();
    private Array<U> graphConnections = new Array<>();
    private Array<V> nodeGroups = new Array<>();

    public DefaultGraph(String type) {
        this.type = type;
    }

    public void addGraphNode(T graphNode) {
        graphNodes.put(graphNode.getId(), graphNode);
    }

    public void removeGraphNode(T graphNode) {
        graphNodes.remove(graphNode.getId());
    }

    public void addGraphConnection(U graphConnection) {
        graphConnections.add(graphConnection);
    }

    public void removeGraphConnection(U graphConnection) {
        graphConnections.removeValue(graphConnection, false);
    }

    public void addNodeGroup(V nodeGroup) {
        nodeGroups.add(nodeGroup);
    }

    public void removeNodeGroup(V nodeGroup) {
        nodeGroups.removeValue(nodeGroup, false);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public T getNodeById(String id) {
        return graphNodes.get(id);
    }

    @Override
    public Iterable<? extends T> getNodes() {
        return graphNodes.values();
    }

    @Override
    public Iterable<? extends U> getConnections() {
        return graphConnections;
    }

    @Override
    public Iterable<? extends V> getGroups() {
        return nodeGroups;
    }
}
