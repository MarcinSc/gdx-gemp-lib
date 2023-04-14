package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class DefaultGraphNode implements GraphNode {
    private String nodeId;
    private float x;
    private float y;
    private JsonValue data;
    private NodeConfiguration nodeConfiguration;

    public DefaultGraphNode(String nodeId, float x, float y, JsonValue data, NodeConfiguration nodeConfiguration) {
        this.nodeId = nodeId;
        this.x = x;
        this.y = y;
        this.data = data;
        this.nodeConfiguration = nodeConfiguration;
    }

    @Override
    public String getId() {
        return nodeId;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public JsonValue getData() {
        return data;
    }

    @Override
    public NodeConfiguration getConfiguration() {
        return nodeConfiguration;
    }
}
