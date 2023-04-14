package com.gempukku.libgdx.ui.graph.editor;

public interface GraphNodeEditorInput {
    enum Side {
        Left, Top
    }

    Side getSide();

    float getOffset();

    String getFieldId();
}
