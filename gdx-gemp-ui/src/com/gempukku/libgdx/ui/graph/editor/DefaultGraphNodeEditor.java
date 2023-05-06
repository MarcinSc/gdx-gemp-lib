package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultGraphNodeEditor implements GraphNodeEditor {
    private NodeConfiguration configuration;
    private Label.LabelStyle labelStyle;
    private VisTable table;
    private List<GraphNodeEditorPart> editorParts = new LinkedList<>();
    private Map<String, GraphNodeEditorInput> inputConnectors = new HashMap<>();
    private Map<String, GraphNodeEditorOutput> outputConnectors = new HashMap<>();

    public DefaultGraphNodeEditor(NodeConfiguration configuration) {
        this(configuration, "default");
    }

    public DefaultGraphNodeEditor(NodeConfiguration configuration, String labelStyleName) {
        this(configuration, VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class));
    }

    public DefaultGraphNodeEditor(NodeConfiguration configuration, Label.LabelStyle labelStyle) {
        this.configuration = configuration;
        this.labelStyle = labelStyle;
        table = new VisTable();
    }

    @Override
    public NodeConfiguration getConfiguration() {
        return configuration;
    }

    public void addTopConnector(GraphNodeInput graphNodeInput, Drawable validDrawable, Drawable invalidDrawable) {
        inputConnectors.put(graphNodeInput.getFieldId(), new DefaultGraphNodeEditorInput(GraphNodeInputSide.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, graphNodeInput.getFieldId(), validDrawable, invalidDrawable));
    }

    public void addBottomConnector(GraphNodeOutput graphNodeOutput, Drawable validDrawable, Drawable invalidDrawable) {
        outputConnectors.put(graphNodeOutput.getFieldId(), new DefaultGraphNodeEditorOutput(GraphNodeOutputSide.Bottom,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return table.getWidth() / 2f;
                    }
                }, graphNodeOutput.getFieldId(), validDrawable, invalidDrawable));
    }

    public void addTwoSideGraphPart(
            GraphNodeInput graphNodeInput, Drawable inputValidDrawable, Drawable inputInvalidDrawable,
            GraphNodeOutput graphNodeOutput, Drawable outputValidDrawable, Drawable outputInvalidDrawable) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName(), labelStyle)).grow();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName(), labelStyle);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        DefaultGraphNodeEditorPart graphBoxPart = new DefaultGraphNodeEditorPart(table, null);
        graphBoxPart.setInputConnector(GraphNodeInputSide.Left, graphNodeInput, inputValidDrawable, inputInvalidDrawable);
        graphBoxPart.setOutputConnector(GraphNodeOutputSide.Right, graphNodeOutput, outputValidDrawable, outputInvalidDrawable);
        addGraphBoxPart(graphBoxPart);
    }

    public void addInputGraphPart(
            GraphNodeInput graphNodeInput, Drawable inputValidDrawable, Drawable inputInvalidDrawable) {
        VisTable table = new VisTable();
        table.add(new VisLabel(graphNodeInput.getFieldName(), labelStyle)).grow().row();

        DefaultGraphNodeEditorPart graphBoxPart = new DefaultGraphNodeEditorPart(table, null);
        graphBoxPart.setInputConnector(GraphNodeInputSide.Left, graphNodeInput, inputValidDrawable, inputInvalidDrawable);
        addGraphBoxPart(graphBoxPart);
    }

    public void addOutputGraphPart(
            GraphNodeOutput graphNodeOutput, Drawable outputValidDrawable, Drawable outputInvalidDrawable) {
        VisTable table = new VisTable();
        VisLabel outputLabel = new VisLabel(graphNodeOutput.getFieldName(), labelStyle);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        DefaultGraphNodeEditorPart graphBoxPart = new DefaultGraphNodeEditorPart(table, null);
        graphBoxPart.setOutputConnector(GraphNodeOutputSide.Right, graphNodeOutput, outputValidDrawable, outputInvalidDrawable);
        addGraphBoxPart(graphBoxPart);
    }

    public void addGraphBoxPart(GraphNodeEditorPart graphBoxPart) {
        editorParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        table.add(actor).growX().row();
        final GraphNodeEditorInput inputConnector = graphBoxPart.getInputConnector();
        if (inputConnector != null) {
            inputConnectors.put(inputConnector.getFieldId(),
                    new DefaultGraphNodeEditorInput(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector.getFieldId(), inputConnector.getDrawable(true), inputConnector.getDrawable(false)));
        }
        final GraphNodeEditorOutput outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.put(outputConnector.getFieldId(),
                    new DefaultGraphNodeEditorOutput(outputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            outputConnector.getFieldId(), outputConnector.getDrawable(true), outputConnector.getDrawable(false)));
        }
    }

    @Override
    public Map<String, GraphNodeEditorInput> getInputs() {
        return inputConnectors;
    }

    @Override
    public Map<String, GraphNodeEditorOutput> getOutputs() {
        return outputConnectors;
    }

    @Override
    public Actor getActor() {
        return table;
    }

    public void initialize(JsonValue data) {
        for (GraphNodeEditorPart editorPart : editorParts) {
            editorPart.initialize(data);
        }
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphNodeEditorPart graphBoxPart : editorParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }
}
