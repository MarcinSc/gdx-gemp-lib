package com.gempukku.libgdx.fbx.stream;

import com.badlogic.gdx.utils.FloatArray;

public class FloatArrayStream implements FloatStream {
    private final FloatArray floatArray;

    public FloatArrayStream(FloatArray floatArray) {
        this.floatArray = floatArray;
    }

    @Override
    public void write(float value) {
        floatArray.add(value);
    }

    @Override
    public void close() {

    }
}
