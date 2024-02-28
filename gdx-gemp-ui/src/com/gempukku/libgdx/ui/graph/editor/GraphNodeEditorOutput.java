package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.graph.data.GraphNodeOutputSide;

public interface GraphNodeEditorOutput extends GraphNodeEditorIO {
    GraphNodeOutputSide getSide();
}
