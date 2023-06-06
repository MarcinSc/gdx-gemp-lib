package com.gempukku.libgdx.fbx.handler.objects.geometry.element;

import com.badlogic.gdx.utils.IntArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.stream.IntArrayStream;
import com.gempukku.libgdx.fbx.stream.IntStream;

public class IntArrayRecordHandler extends UnsupportedValueRecordHandler {
    private Consumer<IntArray> consumer;
    private IntArray intArray;

    public IntArrayRecordHandler(Consumer<IntArray> consumer) {
        this.consumer = consumer;
    }

    @Override
    public IntStream propertyValueIntArray(int arrayLength) {
        intArray = new IntArray(arrayLength);
        return new IntArrayStream(intArray);
    }

    @Override
    public void endOfRecord() {
        consumer.consume(intArray);
    }
}
