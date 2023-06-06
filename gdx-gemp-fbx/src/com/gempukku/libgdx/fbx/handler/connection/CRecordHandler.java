package com.gempukku.libgdx.fbx.handler.connection;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.LongArray;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.UnsupportedValueRecordHandler;

public class CRecordHandler extends UnsupportedValueRecordHandler implements FbxRecordHandler {
    private final Consumer<FbxConnection> connectionConsumer;

    private FbxConnectionType connectionType;
    private Array<String> propertyNames = new Array<>();
    private LongArray objectIds = new LongArray();

    public CRecordHandler(Consumer<FbxConnection> connectionConsumer) {
        this.connectionConsumer = connectionConsumer;
    }

    @Override
    public void propertyValueString(String valueString) {
        if (connectionType == null) {
            connectionType = determineConnectionType(valueString);
        } else {
            propertyNames.add(valueString);
        }
    }

    @Override
    public void propertyValueLong(long valueLong) {
        objectIds.add(valueLong);
    }

    private FbxConnectionType determineConnectionType(String value) {
        switch (value) {
            case "OO":
                return FbxConnectionType.ObjectToObject;
            case "OP":
                return FbxConnectionType.ObjectToProperty;
            case "PO":
                return FbxConnectionType.PropertyToObject;
            case "PP":
                return FbxConnectionType.PropertyToProperty;
        }
        throw new IllegalArgumentException("Unrecognized type of Connection: " + value);
    }

    @Override
    public void endOfRecord() {
        FbxConnection connection = createConnection();
        connectionConsumer.consume(connection);
    }

    private FbxConnection createConnection() {
        switch (connectionType) {
            case ObjectToObject:
                return new FbxConnection(connectionType, objectIds.get(0), null, objectIds.get(1), null);
            case ObjectToProperty:
                return new FbxConnection(connectionType, objectIds.get(0), null, objectIds.get(1), propertyNames.get(0));
            case PropertyToObject:
                return new FbxConnection(connectionType, objectIds.get(0), propertyNames.get(0), objectIds.get(1), null);
            case PropertyToProperty:
                return new FbxConnection(connectionType, objectIds.get(0), propertyNames.get(0), objectIds.get(1), propertyNames.get(1));
        }
        return null;
    }
}
