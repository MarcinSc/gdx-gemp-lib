package com.gempukku.libgdx.common;

public interface BiPredicate<FirstValue, SecondValue> {
    boolean test(FirstValue firstValue, SecondValue secondValue);
}
