package com.gempukku.libgdx.fbx.handler.objects.model;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;

public class FbxModel {
    private Array<Object> values;
    private int version;
    private Array<FbxProperty> properties;
    private boolean shading;
    private String culling;

    public FbxModel(Array<Object> values, int version, Array<FbxProperty> properties, boolean shading, String culling) {
        this.values = values;
        this.version = version;
        this.properties = properties;
        this.shading = shading;
        this.culling = culling;
    }

    public Array<Object> getValues() {
        return values;
    }

    public int getVersion() {
        return version;
    }

    public Array<FbxProperty> getProperties() {
        return properties;
    }

    public boolean getShading() {
        return shading;
    }

    public String getCulling() {
        return culling;
    }
}
