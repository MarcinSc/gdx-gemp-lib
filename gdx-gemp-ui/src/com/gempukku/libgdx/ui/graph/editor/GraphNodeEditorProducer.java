package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;


public interface GraphNodeEditorProducer {
    String getName();

    boolean isCloseable();

    GraphNodeEditor createNodeEditor(Skin skin, JsonValue data);
}
