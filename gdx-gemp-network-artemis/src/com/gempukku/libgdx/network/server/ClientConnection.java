package com.gempukku.libgdx.network.server;

import com.artemis.Entity;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public interface ClientConnection {
    String getName();

    void entityAdded(String entityId, Entity entityData);

    void entityModified(String entityId, Entity entityData);

    void entityRemoved(String entityId, Entity entityData);

    void eventSent(String entityId, Entity entity, EntityEvent event);

    void applyChanges();

    void setServerCallback(ServerCallback serverCallback);

    void unsetServerCallback();

    void forceDisconnect();
}
