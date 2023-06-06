package com.gempukku.libgdx.fbx.handler.objects.geometry.element;

import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;

public class StringRecordHandler extends UnsupportedValueRecordHandler implements FbxRecordHandler {
    private Consumer<String> stringConsumer;

    public StringRecordHandler(Consumer<String> stringConsumer) {
        this.stringConsumer = stringConsumer;
    }

    @Override
    public void propertyValueString(String valueString) {
        stringConsumer.consume(valueString);
    }
}
