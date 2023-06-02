package com.gempukku.libgdx.common;

public class ValueProducer<Type> implements Producer<Type> {
    public Type value;

    public ValueProducer(Type value) {
        this.value = value;
    }

    @Override
    public Type create() {
        return value;
    }
}
