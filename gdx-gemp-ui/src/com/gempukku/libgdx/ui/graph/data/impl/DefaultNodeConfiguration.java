package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class DefaultNodeConfiguration implements NodeConfiguration {
    private final String name;
    private final ObjectMap<String, GraphNodeInput> nodeInputs = new OrderedMap<>();
    private final ObjectMap<String, GraphNodeOutput> nodeOutputs = new OrderedMap<>();

    public DefaultNodeConfiguration(String name) {
        this.name = name;
    }

    public void addNodeInput(GraphNodeInput input) {
        nodeInputs.put(input.getFieldId(), input);
    }

    public void addNodeOutput(GraphNodeOutput output) {
        nodeOutputs.put(output.getFieldId(), output);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectMap<String, GraphNodeInput> getNodeInputs() {
        return nodeInputs;
    }

    @Override
    public ObjectMap<String, GraphNodeOutput> getNodeOutputs() {
        return nodeOutputs;
    }
}
