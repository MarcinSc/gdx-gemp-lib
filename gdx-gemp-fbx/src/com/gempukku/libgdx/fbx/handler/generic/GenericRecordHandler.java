package com.gempukku.libgdx.fbx.handler.generic;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.GatherValuesRecordHandler;

public class GenericRecordHandler extends GatherValuesRecordHandler implements FbxRecordHandler {
    private String name;
    private Array<GenericRecordHandler> subHandlers = new Array<>();
    private ObjectMap<String, Array<GenericRecord>> subRecords = new ObjectMap<>();

    public GenericRecordHandler(String name) {
        this.name = name;
    }

    protected boolean shouldIncludeRecord(String name) {
        return true;
    }

    public String getName() {
        return name;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        if (shouldIncludeRecord(name)) {
            GenericRecordHandler handler = new GenericRecordHandler(name);
            subHandlers.add(handler);
            return handler;
        }
        return null;
    }

    @Override
    public void endOfRecord() {
        for (GenericRecordHandler subHandler : subHandlers) {
            Array<GenericRecord> genericRecords = subRecords.get(subHandler.getName());
            if (genericRecords == null) {
                genericRecords = new Array<>();
                subRecords.put(subHandler.getName(), genericRecords);
            }
            genericRecords.add(new GenericRecord(subHandler.getName(), subHandler.values, subHandler.getSubRecords()));
        }
        subHandlers.clear();
    }

    public ObjectMap<String, Array<GenericRecord>> getSubRecords() {
        return subRecords;
    }
}
