package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class DefaultGraphNode implements GraphNode {
    private String nodeId;
    private String type;
    private float x;
    private float y;
    private JsonValue data;

    public DefaultGraphNode(String nodeId, String type, float x, float y, JsonValue data) {
        this.nodeId = nodeId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.data = data;
    }

    @Override
    public String getId() {
        return nodeId;
    }

    @Override
    public String getType() {
        return type;
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
}
