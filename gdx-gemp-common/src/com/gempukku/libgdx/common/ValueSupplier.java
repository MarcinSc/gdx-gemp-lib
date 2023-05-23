package com.gempukku.libgdx.common;

public class ValueSupplier<T> implements Supplier<T> {
    public T value;

    public ValueSupplier(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
