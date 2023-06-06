package com.gempukku.libgdx.fbx.handler.connection;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;

public class ConnectionsRecordHandler extends GenericRecordHandler implements FbxRecordHandler, Function<String, FbxRecordHandler> {
    private Array<FbxConnection> connections = new Array<>();

    public ConnectionsRecordHandler() {
        super("Connections");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("Connections"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        if (name.equals("C"))
            return new CRecordHandler(new Consumer<FbxConnection>() {
                @Override
                public void consume(FbxConnection fbxConnection) {
                    connections.add(fbxConnection);
                }
            });
        return super.newRecord(name);
    }

    public Array<FbxConnection> getConnections() {
        return connections;
    }
}
