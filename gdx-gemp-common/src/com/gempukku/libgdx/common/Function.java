package com.gempukku.libgdx.common;

public interface Function<T, U> {
    U evaluate(T value);
}
