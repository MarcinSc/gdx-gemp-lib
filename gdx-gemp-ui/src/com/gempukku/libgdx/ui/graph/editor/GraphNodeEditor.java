package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

import java.util.Map;

public interface GraphNodeEditor extends Disposable {
    Actor getActor();

    JsonValue getData();

    NodeConfiguration getConfiguration();

    Map<String, GraphNodeEditorInput> getInputs();

    Map<String, GraphNodeEditorOutput> getOutputs();

    void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph graph);
}