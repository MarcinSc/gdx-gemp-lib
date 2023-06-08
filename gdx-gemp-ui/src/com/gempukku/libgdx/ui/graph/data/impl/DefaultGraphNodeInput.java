package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Predicate;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;

import java.util.Collection;

public class DefaultGraphNodeInput implements GraphNodeInput {
    private final String id;
    private final String name;
    private final boolean acceptingMultiple;
    private final Predicate<String> acceptedTypePredicate;
    private final boolean required;
    private final GraphNodeInputSide side;

    public DefaultGraphNodeInput(String id, String name, String... acceptedType) {
        this(id, name, new ArrayContainsPredicate<>(acceptedType));
    }

    public DefaultGraphNodeInput(String id, String name, Predicate<String> acceptedTypePredicate) {
        this(id, name, false, acceptedTypePredicate);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, String... acceptedType) {
        this(id, name, required, new ArrayContainsPredicate<>(acceptedType));
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, Predicate<String> acceptedTypePredicate) {
        this(id, name, required, GraphNodeInputSide.Left, acceptedTypePredicate);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, GraphNodeInputSide side, String... acceptedType) {
        this(id, name, required, side, new ArrayContainsPredicate<>(acceptedType));
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, GraphNodeInputSide side, Predicate<String> acceptedTypePredicate) {
        this(id, name, required, side, false, acceptedTypePredicate);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, GraphNodeInputSide side, boolean acceptingMultiple, String... acceptedType) {
        this(id, name, required, side, acceptingMultiple, new ArrayContainsPredicate<>(acceptedType));
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, GraphNodeInputSide side, boolean acceptingMultiple, Predicate<String> acceptedTypePredicate) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.side = side;
        this.acceptingMultiple = acceptingMultiple;
        this.acceptedTypePredicate = acceptedTypePredicate;
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
    public boolean acceptsFieldType(String fieldType) {
        return acceptedTypePredicate.test(fieldType);
    }
}
