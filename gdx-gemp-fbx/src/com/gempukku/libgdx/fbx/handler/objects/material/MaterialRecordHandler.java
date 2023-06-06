package com.gempukku.libgdx.fbx.handler.objects.material;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class MaterialRecordHandler extends GenericRecordHandler implements Function<String, FbxRecordHandler> {
    private int version;
    private String shadingModel;
    private int multiLayer;
    private Array<FbxProperty> properties = new Array<>();

    private Array<FbxMaterial> materials = new Array<>();

    public MaterialRecordHandler() {
        super("Material");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("Material"))
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
            case "ShadingModel":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        shadingModel = valueString;
                    }
                };
            case "MultiLayer":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueInt(int valueInt) {
                        multiLayer = valueInt;
                    }
                };
            case "Properties70":
                return new PropertiesRecordHandler(new Consumer<Array<FbxProperty>>() {
                    @Override
                    public void consume(Array<FbxProperty> fbxProperties) {
                        properties.addAll(fbxProperties);
                    }
                });
        }
        return super.newRecord(name);
    }

    @Override
    public void endOfRecord() {
        super.endOfRecord();

        materials.add(new FbxMaterial(values, version, shadingModel, multiLayer, properties));
        values = new Array<>();
        version = 0;
        shadingModel = null;
        multiLayer = 0;
        properties = new Array<>();
    }
}
