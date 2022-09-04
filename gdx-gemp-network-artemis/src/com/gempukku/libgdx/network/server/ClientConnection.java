package com.gempukku.libgdx.network.server;

import com.artemis.Entity;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public interface ClientConnection {
    String getName();

    void entityAdded(Entity entityData);

    void entityModified(Entity entityData);

    void entityRemoved(int entityId);

    void eventSent(int entityId, EntityEvent event);

    void applyChanges();

    void setServerCallback(ServerCallback serverCallback);

    void unsetServerCallback();

    void forceDisconnect();
}
