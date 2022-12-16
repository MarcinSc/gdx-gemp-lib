package com.gempukku.libgdx.network.id;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.network.server.EntityIdMapper;

public class ServerEntityIdSystem extends EntitySystem implements EntityIdMapper {
    private ComponentMapper<ServerEntityIdComponent> serverEntityIdComponentMapper;

    private ObjectMap<String, Entity> entityMap = new ObjectMap<>();
    private ObjectSet<Entity> removed = new ObjectSet<>();
    private IdGenerator idGenerator;

    public ServerEntityIdSystem(IdGenerator idGenerator) {
        super(Aspect.all());
        this.idGenerator = idGenerator;
    }

    @Override
    public void inserted(Entity entity) {
        if (!serverEntityIdComponentMapper.has(entity)) {
            assignEntityId(entity);
        }
    }

    private String assignEntityId(Entity entity) {
        ServerEntityIdComponent id = serverEntityIdComponentMapper.create(entity);
        String entityId;
        do {
            entityId = idGenerator.generateId();
        } while (entityMap.containsKey(entityId));
        id.setId(entityId);
        entityMap.put(entityId, entity);
        return entityId;
    }

    @Override
    public void removed(Entity entity) {
        removed.add(entity);
    }

    @Override
    public Entity findfromId(String entityId) {
        return entityMap.get(entityId);
    }

    @Override
    public String getEntityId(Entity entity) {
        ServerEntityIdComponent entityId = entity.getComponent(ServerEntityIdComponent.class);
        if (entityId == null)
            return assignEntityId(entity);
        return entityId.getId();
    }

    @Override
    protected void processSystem() {
        for (Entity entity : removed) {
            ServerEntityIdComponent id = serverEntityIdComponentMapper.get(entity);
            if (id != null) {
                String entityId = id.getId();
                entityMap.remove(entityId);
                serverEntityIdComponentMapper.remove(entity);
            }
        }
        removed.clear();
    }
}
