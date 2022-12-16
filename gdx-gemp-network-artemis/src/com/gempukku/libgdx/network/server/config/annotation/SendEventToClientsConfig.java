package com.gempukku.libgdx.network.server.config.annotation;

import com.artemis.Entity;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.server.ClientConnection;
import com.gempukku.libgdx.network.server.config.NetworkEntityConfig;

public class SendEventToClientsConfig implements NetworkEntityConfig {
    @Override
    public boolean isEntitySentToAll(Entity entity) {
        return false;
    }

    @Override
    public boolean isEntitySentToClient(Entity entity, ClientConnection clientConnection) {
        return false;
    }

    @Override
    public boolean isEventSentToAll(EntityEvent event) {
        return event.getClass().getAnnotation(SendToClients.class) != null;
    }

    @Override
    public boolean isEventSentToClient(EntityEvent event, ClientConnection clientConnection) {
        return false;
    }
}
