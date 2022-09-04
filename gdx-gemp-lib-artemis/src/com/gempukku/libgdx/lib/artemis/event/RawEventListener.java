package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.Entity;

public interface RawEventListener {
    void eventDispatched(EntityEvent event, Entity entity);
}
