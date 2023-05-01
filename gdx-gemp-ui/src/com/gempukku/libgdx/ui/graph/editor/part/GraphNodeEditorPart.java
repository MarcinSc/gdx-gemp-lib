package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;

public interface GraphNodeEditorPart {
    GraphNodeEditorOutput getOutputConnector();

    GraphNodeEditorInput getInputConnector();

    Actor getActor();

    void initialize(JsonValue data);

    void serializePart(JsonValue value);
}
