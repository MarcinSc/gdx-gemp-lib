package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.data.impl.NamedGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.NamedGraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.impl.NamedNodeConfiguration;

import java.util.Iterator;

public abstract class DefaultGraphNodeEditorProducer implements GraphNodeEditorProducer {
    private final NamedNodeConfiguration configuration;

    public DefaultGraphNodeEditorProducer(NamedNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public DefaultGraphNodeEditor createNodeEditor(String nodeId, JsonValue data) {
        DefaultGraphNodeEditor nodeEditor = new DefaultGraphNodeEditor(configuration);
        addConfigurationInputsAndOutputs(nodeEditor);
        buildNodeEditor(nodeEditor, configuration);
        if (data != null)
            nodeEditor.initialize(data);

        return nodeEditor;
    }

    protected abstract Drawable getInputDrawable(GraphNodeInputSide side, boolean valid);

    protected abstract Drawable getOutputDrawable(GraphNodeOutputSide side, boolean valid);

    protected abstract void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, NodeConfiguration configuration);

    protected void addConfigurationInputsAndOutputs(DefaultGraphNodeEditor nodeEditor) {
        Iterator<? extends NamedGraphNodeInput> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<? extends NamedGraphNodeOutput> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            NamedGraphNodeInput input = null;
            NamedGraphNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.getSide() == GraphNodeInputSide.Top) {
                    nodeEditor.addTopConnector(input, getInputDrawable(input.getSide(), true), getInputDrawable(input.getSide(), false));
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.getSide() == GraphNodeOutputSide.Bottom) {
                    nodeEditor.addBottomConnector(output, getOutputDrawable(output.getSide(), true), getOutputDrawable(output.getSide(), false));
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                nodeEditor.addTwoSideGraphPart(
                        input, getInputDrawable(input.getSide(), true), getInputDrawable(input.getSide(), false),
                        output, getOutputDrawable(output.getSide(), true), getOutputDrawable(output.getSide(), false));
            } else if (input != null) {
                nodeEditor.addInputGraphPart(
                        input, getInputDrawable(input.getSide(), true), getInputDrawable(input.getSide(), false));
            } else if (output != null) {
                nodeEditor.addOutputGraphPart(
                        output, getOutputDrawable(output.getSide(), true), getOutputDrawable(output.getSide(), false));
            }
        }
    }
}
