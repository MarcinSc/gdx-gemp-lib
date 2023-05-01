package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;

public class NodePropertyChangedEvent<T> extends Event {
    private String nodeId;
    private String property;
    private T oldValue;
    private T newValue;

    public NodePropertyChangedEvent(String nodeId, String property, T oldValue, T newValue) {
        this.nodeId = nodeId;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getProperty() {
        return property;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
