package com.gempukku.libgdx.network.server;

public interface RemoteHandler {
    void addClientConnection(ClientConnection clientConnection);

    void removeClientConnection(ClientConnection clientConnection);

    void disconnectAllClients();
}
