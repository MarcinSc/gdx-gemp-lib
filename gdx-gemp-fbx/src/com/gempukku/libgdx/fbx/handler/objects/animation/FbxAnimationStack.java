package com.gempukku.libgdx.fbx.handler.objects.animation;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;

public class FbxAnimationStack {
    private Array<Object> values;
    private Array<FbxProperty> properties;

    public FbxAnimationStack(Array<Object> values, Array<FbxProperty> properties) {
        this.values = values;
        this.properties = properties;
    }

    public Array<Object> getValues() {
        return values;
    }

    public Array<FbxProperty> getProperties() {
        return properties;
    }
}
