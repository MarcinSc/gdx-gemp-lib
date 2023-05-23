package com.gempukku.libgdx.common;

public class ValueProducer<T> implements Producer<T> {
    public T value;

    public ValueProducer(T value) {
        this.value = value;
    }

    @Override
    public T create() {
        return value;
    }
}
