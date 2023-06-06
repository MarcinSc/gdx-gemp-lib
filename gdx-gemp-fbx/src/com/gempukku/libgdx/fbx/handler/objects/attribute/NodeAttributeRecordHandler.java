package com.gempukku.libgdx.fbx.handler.objects.attribute;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.GatherValuesRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class NodeAttributeRecordHandler extends GatherValuesRecordHandler implements FbxRecordHandler, Function<String, FbxRecordHandler> {
    private Array<FbxNodeAttribute> nodeAttributes = new Array<>();
    private Array<FbxProperty> properties = new Array<>();
    private Array<String> typeFlags = new Array<>();

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("NodeAttribute"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        switch (name) {
            case "Properties70":
                return new PropertiesRecordHandler(new Consumer<Array<FbxProperty>>() {
                    @Override
                    public void consume(Array<FbxProperty> fbxProperties) {
                        properties.addAll(fbxProperties);
                    }
                });
            case "TypeFlags":
                return new UnsupportedValueRecordHandler() {
                    @Override
                    public void propertyValueString(String valueString) {
                        typeFlags.add(valueString);
                    }
                };
        }
        return null;
    }

    @Override
    public void endOfRecord() {
        nodeAttributes.add(new FbxNodeAttribute(values, properties, typeFlags));
        values = new Array<>();
        properties = new Array<>();
        typeFlags = new Array<>();
    }

    public Array<FbxNodeAttribute> getNodeAttributes() {
        return nodeAttributes;
    }
}
