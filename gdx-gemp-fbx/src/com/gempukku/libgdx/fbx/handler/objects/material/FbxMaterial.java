package com.gempukku.libgdx.fbx.handler.objects.material;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;

public class FbxMaterial {
    private Array<Object> values;
    private int version;
    private String shadingModel;
    private int multiLayer;
    private Array<FbxProperty> properties;

    public FbxMaterial(Array<Object> values, int version, String shadingModel, int multiLayer, Array<FbxProperty> properties) {
        this.values = values;
        this.version = version;
        this.shadingModel = shadingModel;
        this.multiLayer = multiLayer;
        this.properties = properties;
    }

    public Array<Object> getValues() {
        return values;
    }

    public int getVersion() {
        return version;
    }

    public String getShadingModel() {
        return shadingModel;
    }

    public int getMultiLayer() {
        return multiLayer;
    }

    public Array<FbxProperty> getProperties() {
        return properties;
    }
}
