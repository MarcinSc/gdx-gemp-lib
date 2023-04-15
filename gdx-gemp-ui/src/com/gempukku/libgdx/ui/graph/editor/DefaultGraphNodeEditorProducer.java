package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

import java.util.Iterator;

public class DefaultGraphNodeEditorProducer implements GraphNodeEditorProducer {
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
    public NodeConfiguration getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    public DefaultGraphNodeEditor createNodeEditor(Skin skin, JsonValue data) {
        DefaultGraphNodeEditor nodeEditor = createNodeEditor(skin, configuration);
        addConfigurationInputsAndOutputs(nodeEditor);
        if (data != null)
            nodeEditor.initialize(data);

        return nodeEditor;
    }

    protected DefaultGraphNodeEditor createNodeEditor(Skin skin, NodeConfiguration configuration) {
        return new DefaultGraphNodeEditor(configuration);
    }

    protected void addConfigurationInputsAndOutputs(DefaultGraphNodeEditor nodeEditor) {
        Iterator<GraphNodeInput> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<GraphNodeOutput> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput input = null;
            GraphNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    nodeEditor.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
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
