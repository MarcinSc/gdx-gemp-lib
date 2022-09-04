package com.gempukku.libgdx.network;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class EventOnEntity {
    private int entityId;
    private EntityEvent entityEvent;

    public EventOnEntity(int entityId, EntityEvent entityEvent) {
        this.entityId = entityId;
        this.entityEvent = entityEvent;
    }

    public int getEntityId() {
        return entityId;
    }

    public EntityEvent getEntityEvent() {
        return entityEvent;
    }
}
