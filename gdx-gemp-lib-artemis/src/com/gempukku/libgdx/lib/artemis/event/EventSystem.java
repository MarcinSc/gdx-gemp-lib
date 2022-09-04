package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.BaseSystem;
import com.artemis.Entity;

import java.util.ArrayList;
import java.util.List;

public class EventSystem extends BaseSystem {
    private EntityEventDispatcher entityEventDispatcher;
    private List<RawEventListener> rawEventListeners = new ArrayList<>();

    public EventSystem(EntityEventDispatcher entityEventDispatcher) {
        this.entityEventDispatcher = entityEventDispatcher;
    }

    @Override
    protected void initialize() {
        entityEventDispatcher.initializeForWorld(world);
    }

    public void addRawEventListener(RawEventListener rawEventListener) {
        rawEventListeners.add(rawEventListener);
    }

    public void removeRawEventListener(RawEventListener rawEventListener) {
        rawEventListeners.remove(rawEventListener);
    }

    public void fireEvent(EntityEvent event, Entity entity) {
        entityEventDispatcher.dispatchEvent(event, entity);
        for (RawEventListener rawEventListener : rawEventListeners) {
            rawEventListener.eventDispatched(event, entity);
        }
    }

    @Override
    protected void processSystem() {

    }
}
