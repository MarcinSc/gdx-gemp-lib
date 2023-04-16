package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

import java.util.Map;

public interface GraphNodeEditor {
    Actor getActor();

    JsonValue getData();

    NodeConfiguration getConfiguration();

    Map<String, GraphNodeEditorInput> getInputs();

    Map<String, GraphNodeEditorOutput> getOutputs();
}
