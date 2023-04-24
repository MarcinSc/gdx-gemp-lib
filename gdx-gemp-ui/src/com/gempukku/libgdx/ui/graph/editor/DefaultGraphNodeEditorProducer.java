package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.*;

import java.util.Iterator;

public abstract class DefaultGraphNodeEditorProducer implements GraphNodeEditorProducer {
    private final NodeConfiguration configuration;

    public DefaultGraphNodeEditorProducer(NodeConfiguration configuration) {
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
    public DefaultGraphNodeEditor createNodeEditor(JsonValue data) {
        DefaultGraphNodeEditor nodeEditor = new DefaultGraphNodeEditor(configuration);
        addConfigurationInputsAndOutputs(nodeEditor);
        buildNodeEditor(nodeEditor, configuration);
        if (data != null)
            nodeEditor.initialize(data);

        return nodeEditor;
    }

    protected abstract void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, NodeConfiguration configuration);

    protected void addConfigurationInputsAndOutputs(DefaultGraphNodeEditor nodeEditor) {
        Iterator<GraphNodeInput> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<GraphNodeOutput> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput input = null;
            GraphNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.getSide() == GraphNodeInputSide.Top) {
                    nodeEditor.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.getSide() == GraphNodeOutputSide.Bottom) {
                    nodeEditor.addBottomConnector(output);
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                nodeEditor.addTwoSideGraphPart(input, output);
            } else if (input != null) {
                nodeEditor.addInputGraphPart(input);
            } else if (output != null) {
                nodeEditor.addOutputGraphPart(output);
            }
        }
    }
}
