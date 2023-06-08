package com.gempukku.libgdx.ui.graph.data;

public interface GraphNodeInput extends GraphNodeIO {
    GraphNodeInputSide getSide();

    boolean acceptsFieldType(String fieldType);
}
