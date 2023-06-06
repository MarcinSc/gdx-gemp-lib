package com.gempukku.libgdx.fbx.handler.objects.model;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class ModelRecordHandler extends GenericRecordHandler implements Function<String, FbxRecordHandler> {
    private int version;
    private Array<FbxProperty> properties = new Array<>();
    private boolean shading;
    private String culling;

    private Array<FbxModel> models = new Array<>();

    public ModelRecordHandler() {
        super("Model");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("Model"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        switch (name) {
            case "Version":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueInt(int valueInt) {
                        version = valueInt;
                    }
                };
            case "Properties70":
                return new PropertiesRecordHandler(new Consumer<Array<FbxProperty>>() {
                    @Override
                    public void consume(Array<FbxProperty> fbxProperties) {
                        properties.addAll(fbxProperties);
                    }
                });
            case "Shading":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueBoolean(boolean valueBoolean) {
                        shading = valueBoolean;
                    }
                };
            case "Culling":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        culling = valueString;
                    }
                };
        }
        return super.newRecord(name);
    }

    @Override
    public void endOfRecord() {
        super.endOfRecord();

        models.add(new FbxModel(values, version, properties, shading, culling));
        values = new Array<>();
        version = -1;
        properties = new Array<>();
        shading = false;
        culling = null;
    }

    public Array<FbxModel> getModels() {
        return models;
    }
}
