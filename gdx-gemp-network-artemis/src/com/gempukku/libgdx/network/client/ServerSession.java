package com.gempukku.libgdx.network.client;

import com.gempukku.libgdx.network.NetworkMessage;

public interface ServerSession<T> {
    void sendMessage(NetworkMessage<T> networkMessage);

    void disconnect();
}
