package com.gempukku.libgdx.ui.graph.data;

public interface GraphNodeInput extends GraphNodeIO {
    boolean isRequired();

    GraphNodeInputSide getSide();
}
