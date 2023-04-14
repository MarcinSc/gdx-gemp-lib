package com.gempukku.libgdx.ui.graph;

import java.util.Objects;

public class NodeConnector {
    private final String nodeId;
    private final String fieldId;

    public NodeConnector(String nodeId, String fieldId) {
        this.nodeId = nodeId;
        this.fieldId = fieldId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getFieldId() {
        return fieldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeConnector that = (NodeConnector) o;
        return Objects.equals(nodeId, that.nodeId) && Objects.equals(fieldId, that.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, fieldId);
    }
}
