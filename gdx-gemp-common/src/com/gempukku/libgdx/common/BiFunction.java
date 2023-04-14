package com.gempukku.libgdx.common;

public interface BiFunction<T, U, V> {
    V evaluate(T value1, U value2);
}
