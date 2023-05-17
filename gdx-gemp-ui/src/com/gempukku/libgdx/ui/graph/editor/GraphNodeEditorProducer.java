package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.utils.JsonValue;


public interface GraphNodeEditorProducer {
    String getName();

    boolean isCloseable();

    GraphNodeEditor createNodeEditor(String nodeId, JsonValue data);
}
