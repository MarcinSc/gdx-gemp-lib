package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;

public class DefaultGraphNodeOutput implements GraphNodeOutput {
    private final String id;
    private final String name;
    private GraphNodeOutputSide side;
    private final Function<ObjectMap<String, Array<String>>, String> outputTypeFunction;
    private final Array<String> propertyTypes;

    public DefaultGraphNodeOutput(String id, String name, final String producedType) {
        this(id, name, GraphNodeOutputSide.Right, producedType);
    }

    public DefaultGraphNodeOutput(String id, String name, GraphNodeOutputSide side, final String producedType) {
        this(id, name, side, null, producedType);
    }

    public DefaultGraphNodeOutput(String id, String name, Function<ObjectMap<String, Array<String>>, String> outputTypeFunction, String... producedType) {
        this(id, name, GraphNodeOutputSide.Right, outputTypeFunction, producedType);
    }

    public DefaultGraphNodeOutput(String id, String name, GraphNodeOutputSide side, Function<ObjectMap<String, Array<String>>, String> outputTypeFunction, final String... producedType) {
        this.id = id;
        this.name = name;
        this.side = side;
        if (outputTypeFunction == null) {
            outputTypeFunction = new Function<ObjectMap<String, Array<String>>, String>() {
                @Override
                public String evaluate(ObjectMap<String, Array<String>> value) {
                    return producedType[0];
                }
            };
        }
        this.outputTypeFunction = outputTypeFunction;
        this.propertyTypes = new Array<>(producedType);
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public String determineFieldType(ObjectMap<String, Array<String>> inputs) {
        return outputTypeFunction.evaluate(inputs);
    }

    @Override
    public boolean acceptsMultipleConnections() {
        return getSide() != GraphNodeOutputSide.Bottom;
    }

    @Override
    public Array<String> getConnectableFieldTypes() {
        return propertyTypes;
    }

    @Override
    public GraphNodeOutputSide getSide() {
        return side;
    }
}
