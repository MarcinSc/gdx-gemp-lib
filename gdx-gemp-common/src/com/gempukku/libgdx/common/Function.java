package com.gempukku.libgdx.common;

public interface Function<Value, Result> {
    Result evaluate(Value value);
}
