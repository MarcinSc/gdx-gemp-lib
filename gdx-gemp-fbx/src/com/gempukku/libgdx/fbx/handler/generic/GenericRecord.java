package com.gempukku.libgdx.fbx.handler.generic;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class GenericRecord {
    private String name;
    private Array<Object> values;
    private ObjectMap<String, Array<GenericRecord>> records;

    public GenericRecord(String name, Array<Object> values, ObjectMap<String, Array<GenericRecord>> records) {
        this.name = name;
        this.values = values;
        this.records = records;
    }

    public String getName() {
        return name;
    }

    public Array<Object> getValues() {
        return values;
    }

    public ObjectMap<String, Array<GenericRecord>> getRecords() {
        return records;
    }
}
