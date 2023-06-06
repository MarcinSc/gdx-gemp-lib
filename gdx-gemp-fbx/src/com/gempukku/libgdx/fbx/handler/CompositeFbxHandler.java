package com.gempukku.libgdx.fbx.handler;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecord;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;

public class CompositeFbxHandler implements FbxHandler {
    private ObjectMap<String, Function<String, FbxRecordHandler>> recordHandlerMap = new ObjectMap<>();
    private Array<GenericRecord> records = new Array<>();

    public void setRecordHandler(String name, Function<String, FbxRecordHandler> recordHandlerProducer) {
        recordHandlerMap.put(name, recordHandlerProducer);
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        Function<String, FbxRecordHandler> recordHandler = recordHandlerMap.get(name);
        if (recordHandler != null) {
            return recordHandler.evaluate(name);
        } else {
            return new GenericRecordHandler(name) {
                @Override
                public void endOfRecord() {
                    super.endOfRecord();
                    records.addAll(getRecords());
                }
            };
        }
    }

    public Array<GenericRecord> getRecords() {
        return records;
    }
}
