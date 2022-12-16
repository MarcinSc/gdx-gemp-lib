package com.gempukku.libgdx.network.client;

import com.github.czyzby.websocket.WebSocket;

public interface ServerSessionProducer<T> {
    ServerSession<T> createServerSession(WebSocket socket);
}
