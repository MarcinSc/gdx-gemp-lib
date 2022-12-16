package com.gempukku.libgdx.network.client;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.utils.ObjectMap;

public class ServerEntitySystem extends EntitySystem {
    private ObjectMap<String, Entity> entityMap = new ObjectMap<>();

    public ServerEntitySystem() {
        super(Aspect.all(ServerEntityComponent.class));
    }

    @Override
    public void inserted(Entity e) {
        entityMap.put(e.getComponent(ServerEntityComponent.class).getEntityId(), e);
    }

    @Override
    public void removed(Entity e) {
        entityMap.remove(e.getComponent(ServerEntityComponent.class).getEntityId());
    }

    public Entity findEntity(String entityId) {
        return entityMap.get(entityId);
    }

    @Override
    protected void processSystem() {

    }
}
