package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;

import java.util.Collection;

public class DefaultGraphNodeInput implements GraphNodeInput {
    private final String id;
    private final String name;
    private final boolean acceptingMultiple;
    private final Array<String> acceptedTypes;
    private final boolean required;
    private final GraphNodeInputSide side;

    public DefaultGraphNodeInput(String id, String name, String... acceptedType) {
        this(id, name, false, acceptedType);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, String... acceptedType) {
        this(id, name, required, GraphNodeInputSide.Left, acceptedType);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, GraphNodeInputSide side, String... acceptedType) {
        this(id, name, required, side, false, acceptedType);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, GraphNodeInputSide side, boolean acceptingMultiple, String... acceptedType) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.side = side;
        this.acceptingMultiple = acceptingMultiple;
        this.acceptedTypes = new Array<>(acceptedType);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public GraphNodeInputSide getSide() {
        return side;
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
    public boolean acceptsMultipleConnections() {
        return acceptingMultiple;
    }

    @Override
    public Array<String> getConnectableFieldTypes() {
        return acceptedTypes;
    }
}
