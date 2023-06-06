package com.gempukku.libgdx.fbx.stream;

import com.badlogic.gdx.utils.IntArray;

public class IntArrayStream implements IntStream{
    private final IntArray intArray;

    public IntArrayStream(IntArray intArray) {
        this.intArray = intArray;
    }

    @Override
    public void write(int value) {
        intArray.add(value);
    }

    @Override
    public void close() {

    }
}
