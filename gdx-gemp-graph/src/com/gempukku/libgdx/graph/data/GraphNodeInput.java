package com.gempukku.libgdx.graph.data;

public interface GraphNodeInput extends GraphNodeIO {
    GraphNodeInputSide getSide();

    boolean acceptsFieldType(String fieldType);
}
