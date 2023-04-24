package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;

public interface GraphNodeEditorInput {
    GraphNodeInputSide getSide();

    float getOffset();

    String getFieldId();
}
