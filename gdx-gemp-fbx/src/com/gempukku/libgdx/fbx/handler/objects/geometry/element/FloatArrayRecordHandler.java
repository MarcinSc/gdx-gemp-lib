package com.gempukku.libgdx.fbx.handler.objects.geometry.element;

import com.badlogic.gdx.utils.FloatArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;
import com.gempukku.libgdx.fbx.stream.DoubleStream;
import com.gempukku.libgdx.fbx.stream.DoubleToFloatArrayStream;
import com.gempukku.libgdx.fbx.stream.FloatArrayStream;
import com.gempukku.libgdx.fbx.stream.FloatStream;

public class FloatArrayRecordHandler extends UnsupportedValueRecordHandler {
    private Consumer<FloatArray> floatArrayConsumer;
    private FloatArray floatArray;

    public FloatArrayRecordHandler(Consumer<FloatArray> floatArrayConsumer) {
        this.floatArrayConsumer = floatArrayConsumer;
    }

    @Override
    public FloatStream propertyValueFloatArray(int arrayLength) {
        floatArray = new FloatArray(arrayLength);
        return new FloatArrayStream(floatArray);
    }

    @Override
    public DoubleStream propertyValueDoubleArray(int arrayLength) {
        floatArray = new FloatArray(arrayLength);
        return new DoubleToFloatArrayStream(floatArray);
    }

    @Override
    public void endOfRecord() {
        floatArrayConsumer.consume(floatArray);
    }
}
