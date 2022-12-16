package com.gempukku.libgdx.network.json;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.network.client.ServerSession;
import com.gempukku.libgdx.network.client.ServerSessionProducer;
import com.github.czyzby.websocket.WebSocket;

public class JsonValueServerSessionProducer implements ServerSessionProducer<JsonValue> {
    @Override
    public ServerSession<JsonValue> createServerSession(WebSocket socket) {
        return new JsonValueServerSession(socket);
    }
}
