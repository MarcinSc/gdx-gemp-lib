package com.gempukku.libgdx.graph.data;

public interface GraphNodeIO {
    boolean isRequired();

    boolean acceptsMultipleConnections();

    String getFieldId();
}
