package com.gempukku.libgdx.fbx.handler.properties;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.GatherValuesRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;

public class PropertiesRecordHandler extends UnsupportedValueRecordHandler implements FbxRecordHandler, Function<String, FbxRecordHandler> {
    private final Consumer<Array<FbxProperty>> propertiesConsumer;
    private Array<FbxProperty> properties = new Array<>();

    public PropertiesRecordHandler(Consumer<Array<FbxProperty>> propertiesConsumer) {
        this.propertiesConsumer = propertiesConsumer;
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("Properties70"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        if (name.equals("P")) {
            return new GatherValuesRecordHandler() {
                @Override
                public void endOfRecord() {
                    String propertyName = (String) values.get(0);
                    Array<Object> values = new Array<>();
                    for (int i=1; i<this.values.size; i++) {
                        values.add(this.values.get(i));
                    }
                    properties.add(new FbxProperty(propertyName, values));
                }
            };
        }
        return null;
    }

    @Override
    public void endOfRecord() {
        propertiesConsumer.consume(properties);
        properties = new Array<>();
    }
}
