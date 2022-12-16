package com.gempukku.libgdx.network.server.config.annotation;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.server.ClientConnection;
import com.gempukku.libgdx.network.server.config.NetworkEntityConfig;

public class ReplicateToClientsConfig implements NetworkEntityConfig {
    private Bag<Component> tempComponentBag = new Bag<>();

    @Override
    public boolean isEntitySentToAll(Entity entity) {
        loadComponentBag(entity);

        for (Component component : tempComponentBag) {
            if (component.getClass().getAnnotation(ReplicateToClients.class) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEntitySentToClient(Entity entity, ClientConnection clientConnection) {
        return false;
    }

    @Override
    public boolean isEventSentToAll(EntityEvent entityEvent) {
        return false;
    }

    @Override
    public boolean isEventSentToClient(EntityEvent event, ClientConnection clientConnection) {
        return false;
    }

    private void loadComponentBag(Entity entity) {
        tempComponentBag.clear();
        entity.getComponents(tempComponentBag);
    }
}
