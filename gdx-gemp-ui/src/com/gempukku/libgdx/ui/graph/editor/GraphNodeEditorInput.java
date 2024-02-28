package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.graph.data.GraphNodeInputSide;

public interface GraphNodeEditorInput extends GraphNodeEditorIO {
    GraphNodeInputSide getSide();
}
