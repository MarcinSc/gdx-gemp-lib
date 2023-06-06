package com.gempukku.libgdx.fbx.handler.connection;

public class FbxConnection {
    private FbxConnectionType connectionType;
    private long firstId;
    private String firstProperty;
    private long secondId;
    private String secondProperty;

    public FbxConnection(FbxConnectionType connectionType, long firstId, String firstProperty, long secondId, String secondProperty) {
        this.connectionType = connectionType;
        this.firstId = firstId;
        this.firstProperty = firstProperty;
        this.secondId = secondId;
        this.secondProperty = secondProperty;
    }

    public FbxConnectionType getConnectionType() {
        return connectionType;
    }

    public long getFirstId() {
        return firstId;
    }

    public String getFirstProperty() {
        return firstProperty;
    }

    public long getSecondId() {
        return secondId;
    }

    public String getSecondProperty() {
        return secondProperty;
    }
}
