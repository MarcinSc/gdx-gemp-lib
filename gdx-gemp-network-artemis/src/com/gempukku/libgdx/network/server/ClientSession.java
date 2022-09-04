package com.gempukku.libgdx.network.server;

import com.gempukku.libgdx.network.NetworkMessage;

public interface ClientSession<T> {
    void sendMessage(NetworkMessage networkMessage);

    void disconnect();
}
