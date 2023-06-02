package com.gempukku.libgdx.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class LibGDXCollections {
    private final static ObjectMap emptyMap = new ObjectMap();

    public static <Key, Value> ObjectMap<Key, Value> emptyMap() {
        return emptyMap;
    }

    public static <Key, Value> ObjectMap<Key, Value> mapWithOne(Key key, Value value) {
        ObjectMap<Key, Value> result = new ObjectMap<>();
        result.put(key, value);
        return result;
    }

    public static <Key, Value> ObjectMap<Key, Value> mapOf(Array<Key> keys, Array<Value> values) {
        if (keys.size != values.size)
            throw new IllegalArgumentException("Not matching number of keys and values");
        ObjectMap<Key, Value> result = new ObjectMap<>();
        for (int i = 0; i < keys.size; i++) {
            result.put(keys.get(i), values.get(i));
        }
        return result;
    }
}
