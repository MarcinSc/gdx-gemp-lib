package com.gempukku.libgdx.graph.data.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

public class DefaultNodeConfiguration implements NamedNodeConfiguration {
    private final String type;
    private final String name;
    private final ObjectMap<String, NamedGraphNodeInput> nodeInputs = new OrderedMap<>();
    private final ObjectMap<String, NamedGraphNodeOutput> nodeOutputs = new OrderedMap<>();

    public DefaultNodeConfiguration(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public void addNodeInput(NamedGraphNodeInput input) {
        nodeInputs.put(input.getFieldId(), input);
    }

    public void addNodeOutput(NamedGraphNodeOutput output) {
        nodeOutputs.put(output.getFieldId(), output);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ObjectMap<String, NamedGraphNodeInput> getNodeInputs() {
        return nodeInputs;
    }

    @Override
    public ObjectMap<String, NamedGraphNodeOutput> getNodeOutputs() {
        return nodeOutputs;
    }
}
