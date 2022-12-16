package com.gempukku.libgdx.network;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public interface DataSerializer<T> {
    T serializeEvent(EntityEvent entityEvent);

    T serializeComponent(Component component);

    EntityEvent deserializeEntityEvent(T data);

    Component deserializeComponent(Entity entity, World world, T data);
}
