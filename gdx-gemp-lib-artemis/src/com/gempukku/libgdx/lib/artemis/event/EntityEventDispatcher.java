package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.Entity;
import com.artemis.World;

public interface EntityEventDispatcher {
    void initializeForWorld(World world);

    void dispatchEvent(EntityEvent entityEvent, Entity entity);
}
