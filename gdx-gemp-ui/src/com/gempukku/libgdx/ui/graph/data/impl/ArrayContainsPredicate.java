package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Predicate;

public class ArrayContainsPredicate<T> implements Predicate<T> {
    private Array<T> array;

    public ArrayContainsPredicate(T... values) {
        this.array = new Array<>(values);
    }

    @Override
    public boolean test(T t) {
        return array.contains(t, false);
    }
}
