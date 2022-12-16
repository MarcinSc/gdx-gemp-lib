package com.gempukku.libgdx.network.client;


import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.network.DataSerializer;

public class InformationPacketUtil<T> {
    private final World world;
    private final EventSystem eventSystem;
    private final ComponentMapper<ServerEntityComponent> serverEntityMapper;
    private final DataSerializer<T> dataSerializer;
    private final ObjectMap<String, Entity> serverEntityMap = new ObjectMap<>();

    private final Bag<Component> tempComponentBag = new Bag<>();

    public InformationPacketUtil(World world, DataSerializer<T> dataSerializer) {
        this.world = world;
        this.eventSystem = world.getSystem(EventSystem.class);
        serverEntityMapper = world.getMapper(ServerEntityComponent.class);
        this.dataSerializer = dataSerializer;
    }

    public Entity getEntityById(String entityId) {
        return serverEntityMap.get(entityId);
    }

    public String getEntityId(Entity entity) {
        return entity.getComponent(ServerEntityComponent.class).getEntityId();
    }

    public void applyInformationPacket(IncomingInformationPacket<T> packet) {
        if (packet.getType() == IncomingInformationPacket.Type.EVENT) {
            String entityId = packet.getEntityId();
            Entity entity = serverEntityMap.get(entityId);
            if (entity != null) {
                eventSystem.fireEvent(packet.getEvent(), entity);
            }
        } else if (packet.getType() == IncomingInformationPacket.Type.CREATE_ENTITY) {
            Entity entity = world.createEntity();
            ServerEntityComponent serverEntityComponent = serverEntityMapper.create(entity);
            String entityId = packet.getEntityId();
            serverEntityComponent.setEntityId(entityId);
            serverEntityMap.put(entityId, entity);

            for (T entityDatum : packet.getEntityData()) {
                dataSerializer.deserializeComponent(entity, world, entityDatum);
            }
        } else if (packet.getType() == IncomingInformationPacket.Type.MODIFY_ENTITY) {
            Entity entity = serverEntityMap.get(packet.getEntityId());
            if (entity != null) {
                tempComponentBag.clear();
                entity.getComponents(tempComponentBag);
                for (Component component : tempComponentBag) {
                    if (component.getClass().getAnnotation(PreserveComponent.class) == null && !(component instanceof ServerEntityComponent)) {
                        removeComponent(entity, component);
                    }
                }

                for (T entityDatum : packet.getEntityData()) {
                    dataSerializer.deserializeComponent(entity, world, entityDatum);
                }
            }
        } else if (packet.getType() == IncomingInformationPacket.Type.DESTROY_ENTITY) {
            Entity entity = serverEntityMap.remove(packet.getEntityId());
            if (entity != null) {
                world.deleteEntity(entity);
            }
        }
    }

    private void removeComponent(Entity entity, Component component) {
        ComponentMapper<? extends Component> mapper = world.getMapper(component.getClass());
        mapper.remove(entity);
    }
}
