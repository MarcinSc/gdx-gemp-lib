package com.gempukku.libgdx.network.server;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.*;

import java.util.*;

public class SerializingClientConnection<T> implements ClientConnection {
    private final ClientSession<T> session;
    private final DataSerializer<T> dataSerializer;
    private ServerCallback serverCallback;
    private final String username;

    private final Map<Integer, Entity> entitiesCreated = new HashMap<>();
    private final Map<Integer, Entity> entitiesModified = new HashMap<>();
    private final Set<Integer> entitiesRemoved = new HashSet<>();
    private final List<NetworkMessage<T>> eventsToSend = new LinkedList<>();

    private final Bag<Component> tempComponentBag = new Bag<>();

    public SerializingClientConnection(String username, ClientSession<T> session, DataSerializer<T> dataSerializer) {
        this.username = username;
        this.session = session;
        this.dataSerializer = dataSerializer;
    }

    @Override
    public String getName() {
        return username;
    }

    public void eventReceived(int entityId, T eventData) {
        serverCallback.processEvent(username, entityId, (EventFromClient) dataSerializer.deserializeData(eventData));
    }

    @Override
    public void entityAdded(Entity entity) {
        entitiesCreated.put(entity.getId(), entity);
    }

    @Override
    public void entityModified(Entity entity) {
        if (!entitiesCreated.containsKey(entity.getId()))
            entitiesModified.put(entity.getId(), entity);
    }

    @Override
    public void entityRemoved(int entityId) {
        if (entitiesCreated.remove(entityId) == null) {
            entitiesModified.remove(entityId);
            entitiesRemoved.add(entityId);
        }
    }

    @Override
    public void eventSent(int entityId, EntityEvent event) {
        T eventData = dataSerializer.serializeData(event);

        eventsToSend.add(new NetworkMessage<>(entityId, NetworkMessage.Type.EVENT, eventData));
    }

    @Override
    public void applyChanges() {
        if (entitiesRemoved.size() > 0 || entitiesModified.size() > 0
                || entitiesCreated.size() > 0 || eventsToSend.size() > 0) {
            for (int entityId : entitiesRemoved) {
                session.sendMessage(new NetworkMessage<>(entityId, NetworkMessage.Type.ENTITY_REMOVED, (T) null));
            }
            for (Entity entity : entitiesCreated.values()) {
                List<T> entityData = convertToEntityData(entity);
                session.sendMessage(new NetworkMessage<>(entity.getId(), NetworkMessage.Type.ENTITY_CREATED, entityData));
            }
            for (Entity entity : entitiesModified.values()) {
                List<T> entityData = convertToEntityData(entity);
                session.sendMessage(new NetworkMessage<>(entity.getId(), NetworkMessage.Type.ENTITY_MODIFIED, entityData));
            }
            for (NetworkMessage<T> networkMessage : eventsToSend) {
                session.sendMessage(networkMessage);
            }

            entitiesCreated.clear();
            entitiesModified.clear();
            entitiesRemoved.clear();
            eventsToSend.clear();

            session.sendMessage(new NetworkMessage<>(0, NetworkMessage.Type.APPLY_CHANGES, (T) null));
        }
    }

    @Override
    public void setServerCallback(ServerCallback serverCallback) {
        this.serverCallback = serverCallback;
    }

    @Override
    public void unsetServerCallback() {
        this.serverCallback = null;
    }

    private List<T> convertToEntityData(Entity entity) {
        tempComponentBag.clear();
        entity.getComponents(tempComponentBag);

        List<T> entityData = new LinkedList<>();

        for (Component component : tempComponentBag) {
            Class<? extends Component> componentClass = component.getClass();
            if (componentClass.getAnnotation(ReplicateToClients.class) != null
                    || componentClass.getAnnotation(ReplicateWithOthers.class) != null
                    || replicatesToUser(username, componentClass, component)) {
                entityData.add(dataSerializer.serializeData(component));
            }
        }
        return entityData;
    }

    private boolean replicatesToUser(String username, Class<? extends Component> componentClass, Component componentData) {
        ReplicateToOwner replicateToOwner = componentClass.getAnnotation(ReplicateToOwner.class);
        if (replicateToOwner != null) {
            if (componentData instanceof OwnedComponent && ((OwnedComponent) componentData).getOwner().equals(username))
                return true;
            if (componentData instanceof OwnedByMultipleComponent && ((OwnedByMultipleComponent) componentData).getOwners().contains(username))
                return true;
        }
        return false;
    }

    @Override
    public void forceDisconnect() {
        session.disconnect();
    }
}
