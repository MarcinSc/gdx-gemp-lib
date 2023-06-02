package com.gempukku.libgdx.common;

public interface BiFunctionDisposableProducer<FirstValue, SecondValue, Type> {
    Type create(FirstValue firstValue, SecondValue secondValue);

    void dispose(Type disposable);
}
