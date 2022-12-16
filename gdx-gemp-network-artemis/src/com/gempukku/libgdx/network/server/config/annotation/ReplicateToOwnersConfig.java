package com.gempukku.libgdx.network.server.config.annotation;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.server.ClientConnection;
import com.gempukku.libgdx.network.server.config.NetworkEntityConfig;

public class ReplicateToOwnersConfig implements NetworkEntityConfig {
    private Bag<Component> tempComponentBag = new Bag<>();

    @Override
    public boolean isEntitySentToAll(Entity entity) {
        return false;
    }

    @Override
    public boolean isEntitySentToClient(Entity entity, ClientConnection clientConnection) {
        loadComponentBag(entity);

        String username = clientConnection.getName();

        for (Component component : tempComponentBag) {
            Class<? extends Component> componentClass = component.getClass();
            if (componentClass.getAnnotation(ReplicateToOwner.class) != null) {
                if (component instanceof OwnedComponent && ((OwnedComponent) component).isOwnedBy(username))
                    return true;
            }
        }
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
