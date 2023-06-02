package com.gempukku.libgdx.common;

public interface BiFunction<FirstValue, SecondValue, Result> {
    Result evaluate(FirstValue value1, SecondValue value2);
}
