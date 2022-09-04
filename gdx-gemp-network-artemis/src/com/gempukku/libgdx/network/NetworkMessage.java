package com.gempukku.libgdx.network;

import java.util.Collections;
import java.util.List;

public class NetworkMessage<T> {
    public enum Type {
        ENTITY_CREATED, ENTITY_MODIFIED, ENTITY_REMOVED, EVENT, APPLY_CHANGES
    }

    private int entityId;
    private Type type;
    private T payload;
    private List<T> payloadList;

    public NetworkMessage(int entityId, Type type, T payload) {
        this.entityId = entityId;
        this.type = type;
        this.payload = payload;
    }

    public NetworkMessage(int entityId, Type type, List<T> payloadList) {
        this.entityId = entityId;
        this.type = type;
        this.payloadList = payloadList;
    }

    public int getEntityId() {
        return entityId;
    }

    public Type getType() {
        return type;
    }

    public List<T> getPayloadList() {
        if (payload != null)
            return Collections.singletonList(payload);
        return payloadList;
    }
}
