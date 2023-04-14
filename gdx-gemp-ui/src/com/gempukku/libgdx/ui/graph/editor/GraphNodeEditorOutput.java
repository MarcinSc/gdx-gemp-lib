package com.gempukku.libgdx.ui.graph.editor;

public interface GraphNodeEditorOutput {
    enum Side {
        Right, Bottom
    }

    Side getSide();

    float getOffset();

    String getFieldId();
}
