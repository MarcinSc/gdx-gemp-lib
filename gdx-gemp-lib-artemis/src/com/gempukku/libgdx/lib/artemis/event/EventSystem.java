package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.BaseSystem;
import com.artemis.Entity;

public class EventSystem extends BaseSystem {
    private EntityEventDispatcher entityEventDispatcher;

    public EventSystem(EntityEventDispatcher entityEventDispatcher) {
        this.entityEventDispatcher = entityEventDispatcher;
    }

    @Override
    protected void initialize() {
        entityEventDispatcher.initializeForWorld(world);
    }

    public void fireEvent(EntityEvent event, Entity entity) {
        entityEventDispatcher.dispatchEvent(event, entity);
    }

    @Override
    protected void processSystem() {

    }
}
