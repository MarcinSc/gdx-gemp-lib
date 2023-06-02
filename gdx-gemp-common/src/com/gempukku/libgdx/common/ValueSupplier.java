package com.gempukku.libgdx.common;

public class ValueSupplier<Type> implements Supplier<Type> {
    public Type value;

    public ValueSupplier(Type value) {
        this.value = value;
    }

    @Override
    public Type get() {
        return value;
    }
}
