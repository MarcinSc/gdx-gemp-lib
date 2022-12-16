package com.gempukku.libgdx.network.server.config;

import com.artemis.Entity;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.server.ClientConnection;

public interface NetworkEntityConfig {
    boolean isEntitySentToAll(Entity entity);

    boolean isEntitySentToClient(Entity entity, ClientConnection clientConnection);

    boolean isEventSentToAll(EntityEvent event);

    boolean isEventSentToClient(EntityEvent event, ClientConnection clientConnection);
}
