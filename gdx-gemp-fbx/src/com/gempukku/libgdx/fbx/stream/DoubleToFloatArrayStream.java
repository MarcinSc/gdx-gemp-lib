package com.gempukku.libgdx.fbx.stream;

import com.badlogic.gdx.utils.FloatArray;

public class DoubleToFloatArrayStream implements DoubleStream{
    private final FloatArray floatArray;

    public DoubleToFloatArrayStream(FloatArray floatArray) {
        this.floatArray = floatArray;
    }

    @Override
    public void write(double value) {
        floatArray.add((float) value);
    }

    @Override
    public void close() {

    }
}
