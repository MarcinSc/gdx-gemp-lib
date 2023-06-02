package com.gempukku.libgdx.common;

public interface FunctionDisposableProducer<Value, Type> {
    Type create(Value value);

    void dispose(Type disposable);
}
