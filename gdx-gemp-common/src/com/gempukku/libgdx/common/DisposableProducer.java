package com.gempukku.libgdx.common;

public interface DisposableProducer<Type> {
    Type create();

    void dispose(Type disposable);
}
