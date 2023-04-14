package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;


public interface GraphNodeEditorProducer {
    String getName();

    NodeConfiguration getConfiguration();

    boolean isCloseable();

    GraphNodeEditor createNodeEditor(Skin skin, JsonValue data);
}
