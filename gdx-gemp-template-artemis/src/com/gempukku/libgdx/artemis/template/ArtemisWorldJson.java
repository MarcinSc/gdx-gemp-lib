package com.gempukku.libgdx.artemis.template;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Json;

public class ArtemisWorldJson extends Json {
    private World world;
    private Entity entity;

    public void setWorld(World world) {
        this.world = world;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    protected Object newInstance(Class type) {
        if (entity != null && Component.class.isAssignableFrom(type))
            return world.getMapper(type).create(entity);

        return super.newInstance(type);
    }
}
