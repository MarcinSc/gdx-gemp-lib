package com.gempukku.libgdx.fbx.handler.objects.attribute;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;

public class FbxNodeAttribute {
    private Array<Object> values;
    private Array<FbxProperty> properties;
    private Array<String> typeFlags;

    public FbxNodeAttribute(Array<Object> values, Array<FbxProperty> properties, Array<String> typeFlags) {
        this.values = values;
        this.properties = properties;
        this.typeFlags = typeFlags;
    }

    public Array<Object> getValues() {
        return values;
    }

    public Array<FbxProperty> getProperties() {
        return properties;
    }

    public Array<String> getTypeFlags() {
        return typeFlags;
    }
}
