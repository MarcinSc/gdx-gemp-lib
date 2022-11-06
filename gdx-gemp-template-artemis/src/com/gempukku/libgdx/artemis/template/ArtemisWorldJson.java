package com.gempukku.libgdx.artemis.template;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
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
        if (entity != null && Component.class.isAssignableFrom(type)) {
            Class<? extends Component> componentType = (Class<? extends Component>) type;
            ComponentMapper<? extends Component> componentMapper = world.getMapper(componentType);
            if (componentMapper.has(entity))
                throw new GdxRuntimeException("This entity already has the component");
            return componentMapper.create(entity);
        }

        return super.newInstance(type);
    }
}
