package com.gempukku.libgdx.fbx.handler;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;

public class CompositeFbxRecordHandler extends GenericRecordHandler implements FbxRecordHandler, Function<String, FbxRecordHandler> {
    private ObjectMap<String, Function<String, FbxRecordHandler>> recordHandlerMap = new ObjectMap<>();

    public CompositeFbxRecordHandler(String name) {
        super(name);
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        return this;
    }

    public void setRecordHandler(String name, Function<String, FbxRecordHandler> recordHandlerProducer) {
        recordHandlerMap.put(name, recordHandlerProducer);
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        Function<String, FbxRecordHandler> recordHandler = recordHandlerMap.get(name);
        if (recordHandler != null) {
            return recordHandler.evaluate(name);
        } else {
            return super.newRecord(name);
        }
    }
}
