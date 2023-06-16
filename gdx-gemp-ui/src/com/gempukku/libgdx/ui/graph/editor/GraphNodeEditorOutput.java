package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;

public interface GraphNodeEditorOutput extends GraphNodeEditorIO {
    GraphNodeOutputSide getSide();
}
