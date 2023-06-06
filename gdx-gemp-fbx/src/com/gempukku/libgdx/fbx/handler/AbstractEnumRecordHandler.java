package com.gempukku.libgdx.fbx.handler;

import com.gempukku.libgdx.common.Consumer;

public abstract class AbstractEnumRecordHandler<T extends Enum<T>> extends UnsupportedValueRecordHandler {
    private Consumer<T> enumConsumer;

    public AbstractEnumRecordHandler(Consumer<T> enumConsumer) {
        this.enumConsumer = enumConsumer;
    }

    @Override
    public void propertyValueString(String valueString) {
        enumConsumer.consume(mapToEnum(valueString));
    }

    protected abstract T mapToEnum(String valueString);
}
