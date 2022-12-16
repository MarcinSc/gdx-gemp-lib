package com.gempukku.libgdx.network;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class EntityUpdated implements EntityEvent {
    public static EntityUpdated instance = new EntityUpdated();

    private EntityUpdated() {
    }
}
