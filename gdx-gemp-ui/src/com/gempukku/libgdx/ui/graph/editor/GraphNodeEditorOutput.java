package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;

public interface GraphNodeEditorOutput {
    GraphNodeOutputSide getSide();

    float getOffset();

    String getFieldId();
}
