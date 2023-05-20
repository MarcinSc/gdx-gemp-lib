package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;

public class DefaultGraphNodeEditorPart implements GraphNodeEditorPart {
    private final Actor actor;
    private GraphNodeEditorInput inputConnector;
    private GraphNodeEditorOutput outputConnector;
    private final Callback callback;

    public DefaultGraphNodeEditorPart(Actor actor) {
        this(actor, null);
    }

    public DefaultGraphNodeEditorPart(Actor actor, Callback callback) {
        this.actor = actor;
        this.callback = callback;
    }

    public void setInputConnector(GraphNodeInputSide side, GraphNodeInput graphNodeInput, Drawable validDrawable, Drawable invalidDrawable) {
        inputConnector = new DefaultGraphNodeEditorInput(side, null, graphNodeInput.getFieldId(), validDrawable, invalidDrawable);
    }

    public void setOutputConnector(GraphNodeOutputSide side, GraphNodeOutput graphNodeOutput, Drawable validDrawable, Drawable invalidDrawable) {
        outputConnector = new DefaultGraphNodeEditorOutput(side, null, graphNodeOutput.getFieldId(), validDrawable, invalidDrawable);
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return inputConnector;
    }

    @Override
    public GraphNodeEditorOutput getOutputConnector() {
        return outputConnector;
    }

    @Override
    public void initialize(JsonValue data) {
        if (callback != null)
            callback.initialize(data);
    }

    @Override
    public void serializePart(JsonValue object) {
        if (callback != null)
            callback.serialize(object);
    }

    public interface Callback {
        void initialize(JsonValue data);
        void serialize(JsonValue object);
    }
}
