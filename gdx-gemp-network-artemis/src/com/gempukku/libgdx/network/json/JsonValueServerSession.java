package com.gempukku.libgdx.network.json;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.network.NetworkMessage;
import com.gempukku.libgdx.network.client.ServerSession;
import com.github.czyzby.websocket.WebSocket;

public class JsonValueServerSession implements ServerSession<JsonValue> {
    private WebSocket webSocket;

    public JsonValueServerSession(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void sendMessage(NetworkMessage<JsonValue> networkMessage) {
        try {
            JsonValue message = new JsonValue(JsonValue.ValueType.object);
            message.addChild("id", new JsonValue(networkMessage.getEntityId()));
            message.addChild("type", new JsonValue(networkMessage.getType().name()));
            if (networkMessage.getType() == NetworkMessage.Type.EVENT) {
                message.addChild("data", new JsonValue(networkMessage.getPayloadList().get(0).toJson(JsonWriter.OutputType.json)));
            }
            webSocket.send(message.toJson(JsonWriter.OutputType.json));
        } catch (Exception exp) {
            webSocket.close();
        }
    }

    @Override
    public void disconnect() {
        webSocket.close();
    }
}
