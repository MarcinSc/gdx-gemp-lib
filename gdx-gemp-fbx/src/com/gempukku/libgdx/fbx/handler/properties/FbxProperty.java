package com.gempukku.libgdx.fbx.handler.properties;

import com.badlogic.gdx.utils.Array;

public class FbxProperty {
    private String name;
    private Array<Object> values;

    public FbxProperty(String name, Array<Object> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public Array<Object> getValues() {
        return values;
    }
}
