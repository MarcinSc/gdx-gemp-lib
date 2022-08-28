package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.Entity;

public interface EventDispatcher {
    float getPriority();

    void dispatchEvent(EntityEvent event, Entity entity);
}
