package com.gempukku.libgdx.graph.data.impl;

import com.gempukku.libgdx.graph.data.GraphConnection;

import java.util.Objects;

public class DefaultGraphConnection implements GraphConnection {
    private final String nodeFrom;
    private final String fieldFrom;
    private final String nodeTo;
    private final String fieldTo;

    public DefaultGraphConnection(String nodeFrom, String fieldFrom, String nodeTo, String fieldTo) {
        this.nodeFrom = nodeFrom;
        this.fieldFrom = fieldFrom;
        this.nodeTo = nodeTo;
        this.fieldTo = fieldTo;
    }

    @Override
    public String getNodeFrom() {
        return nodeFrom;
    }

    @Override
    public String getFieldFrom() {
        return fieldFrom;
    }

    @Override
    public String getNodeTo() {
        return nodeTo;
    }

    @Override
    public String getFieldTo() {
        return fieldTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultGraphConnection that = (DefaultGraphConnection) o;
        return Objects.equals(nodeFrom, that.nodeFrom) && Objects.equals(fieldFrom, that.fieldFrom) && Objects.equals(nodeTo, that.nodeTo) && Objects.equals(fieldTo, that.fieldTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeFrom, fieldFrom, nodeTo, fieldTo);
    }
}
