package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;

public class DefaultGraphNodeInput implements GraphNodeInput {
    private final String id;
    private final String name;
    private final boolean acceptingMultiple;
    private final Array<String> acceptedTypes;
    private final boolean required;
    private final boolean mainConnection;

    public DefaultGraphNodeInput(String id, String name, String... acceptedType) {
        this(id, name, false, acceptedType);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, String... acceptedType) {
        this(id, name, required, false, acceptedType);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, boolean mainConnection, String... acceptedType) {
        this(id, name, required, mainConnection, false, acceptedType);
    }

    public DefaultGraphNodeInput(String id, String name, boolean required, boolean mainConnection, boolean acceptingMultiple, String... acceptedType) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.mainConnection = mainConnection;
        this.acceptingMultiple = acceptingMultiple;
        this.acceptedTypes = new Array<>(acceptedType);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
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
    public boolean isAcceptingMultiple() {
        return acceptingMultiple;
    }

    @Override
    public Array<String> getAcceptedPropertyTypes() {
        return acceptedTypes;
    }
}
