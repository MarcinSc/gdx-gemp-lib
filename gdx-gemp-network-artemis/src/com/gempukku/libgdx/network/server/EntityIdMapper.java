package com.gempukku.libgdx.network.server;

import com.artemis.Entity;

public interface EntityIdMapper {
    Entity findfromId(String entityId);

    String getEntityId(Entity entity);
}
