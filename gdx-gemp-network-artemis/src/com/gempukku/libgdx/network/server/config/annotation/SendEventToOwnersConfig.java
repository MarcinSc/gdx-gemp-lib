package com.gempukku.libgdx.network.server.config.annotation;

import com.artemis.Entity;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.server.ClientConnection;
import com.gempukku.libgdx.network.server.config.NetworkEntityConfig;

public class SendEventToOwnersConfig implements NetworkEntityConfig {
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
        return false;
    }

    @Override
    public boolean isEventSentToClient(EntityEvent event, ClientConnection clientConnection) {
        if (event.getClass().getAnnotation(SendToOwner.class) != null) {
            String username = clientConnection.getName();
            if (event instanceof OwnedEvent && ((OwnedEvent) event).isOwnedBy(username))
                return true;
        }
        return false;
    }
}
