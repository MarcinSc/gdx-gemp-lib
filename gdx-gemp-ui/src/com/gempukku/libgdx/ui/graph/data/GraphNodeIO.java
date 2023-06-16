package com.gempukku.libgdx.ui.graph.data;

public interface GraphNodeIO {
    boolean isRequired();

    boolean acceptsMultipleConnections();

    String getFieldId();
}
