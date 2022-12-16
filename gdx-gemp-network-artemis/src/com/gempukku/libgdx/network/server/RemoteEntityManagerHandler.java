package com.gempukku.libgdx.network.server;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.event.RawEventListener;
import com.gempukku.libgdx.network.EntityUpdated;
import com.gempukku.libgdx.network.EventFromClient;
import com.gempukku.libgdx.network.server.config.NetworkEntityConfig;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;

public class RemoteEntityManagerHandler extends BaseSystem implements RemoteHandler {
    private EventSystem eventSystem;

    private final Map<String, ClientConnection> clientConnectionMap = new HashMap<>();
    private final Multimap<ClientConnection, String> clientTrackingEntities = HashMultimap.create();
    private final Array<NetworkEntityConfig> networkEntityConfigArray = new Array<>();

    private final EntityIdMapper entityIdMapper;
    private EntitySubscription allEntitiesSubscription;

    public RemoteEntityManagerHandler(EntityIdMapper entityIdMapper) {
        this.entityIdMapper = entityIdMapper;
    }

    @Override
    public void initialize() {
        allEntitiesSubscription = world.getAspectSubscriptionManager().get(Aspect.all());
        allEntitiesSubscription.addSubscriptionListener(
                new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) {
                        for (int i = 0, s = entities.size(); s > i; i++) {
                            entityCreated(entities.get(i));
                        }
                    }

                    @Override
                    public void removed(IntBag entities) {
                        for (int i = 0, s = entities.size(); s > i; i++) {
                            entityRemoved(entities.get(i));
                        }
                    }
                }
        );
        eventSystem.addRawEventListener(
                new RawEventListener() {
                    @Override
                    public void eventDispatched(EntityEvent event, Entity entity) {
                        boolean sendToAllClients = hasToSendToAllClients(event);
                        String entityId = entityIdMapper.getEntityId(entity);
                        for (ClientConnection clientConnection : clientConnectionMap.values()) {
                            if (clientTracksEntity(clientConnection, entityId)) {
                                if (sendToAllClients || hasToSendToClient(event, clientConnection))
                                    clientConnection.eventSent(entityId, entity, event);
                            }
                        }
                    }
                });
    }

    public void addNetworkEntityConfig(NetworkEntityConfig... networkEntityConfigs) {
        for (NetworkEntityConfig networkEntityConfig : networkEntityConfigs) {
            networkEntityConfigArray.add(networkEntityConfig);
        }
    }

    public void removeNetworkEntityConfig(NetworkEntityConfig... networkEntityConfigs) {
        for (NetworkEntityConfig networkEntityConfig : networkEntityConfigs) {
            networkEntityConfigArray.removeValue(networkEntityConfig, true);
        }
    }

    @EventListener
    public void entityUpdated(EntityUpdated entityUpdated, Entity entity) {
        boolean replicateToAllClients = hasToReplicateToAllClients(entity);

        String entityId = entityIdMapper.getEntityId(entity);
        for (Map.Entry<String, ClientConnection> clientConnectionEntry : clientConnectionMap.entrySet()) {
            ClientConnection clientConnection = clientConnectionEntry.getValue();
            if (replicateToAllClients || hasToReplicateToClient(entity, clientConnection)) {
                if (clientTracksEntity(clientConnection, entityId)) {
                    clientConnection.entityModified(entityId, entity);
                } else {
                    clientTrackingEntities.put(clientConnection, entityId);
                    clientConnection.entityAdded(entityId, entity);
                }
            } else if (clientTracksEntity(clientConnection, entityId)) {
                clientConnection.entityRemoved(entityId, entity);
                clientTrackingEntities.remove(clientConnection, entityId);
            }
        }
    }

    private void entityCreated(int entityId) {
        entityUpdated(null, world.getEntity(entityId));
    }

    private void entityRemoved(int entityId) {
        Entity entity = world.getEntity(entityId);
        String entityIdStr = entityIdMapper.getEntityId(entity);
        for (ClientConnection clientConnection : clientConnectionMap.values()) {
            if (clientTracksEntity(clientConnection, entityIdStr)) {
                clientConnection.entityRemoved(entityIdStr, entity);
                clientTrackingEntities.remove(clientConnection, entityIdStr);
            }
        }
    }

    private void broadcastApplyChanges() {
        for (ClientConnection value : clientConnectionMap.values()) {
            value.applyChanges();
        }
    }

    @Override
    public void addClientConnection(final ClientConnection clientConnection) {
        String clientName = clientConnection.getName();

        ClientConnection oldConnection = clientConnectionMap.get(clientName);
        if (oldConnection != null) {
            oldConnection.forceDisconnect();
            clientTrackingEntities.removeAll(oldConnection);
        }
        clientConnectionMap.put(clientName, clientConnection);

        clientConnection.setServerCallback(
                new ServerCallback() {
                    @Override
                    public void processEvent(String fromUser, String entityId, EventFromClient event) {
                        Entity trackedEntity = findTrackedEntity(entityId);
                        if (trackedEntity != null) {
                            event.setOrigin(fromUser);
                            eventSystem.fireEvent(event, trackedEntity);
                        }
                    }

                    private Entity findTrackedEntity(String entityId) {
                        if (clientTracksEntity(clientConnection, entityId))
                            return entityIdMapper.findfromId(entityId);
                        return null;
                    }
                });

        IntBag entities = allEntitiesSubscription.getEntities();
        for (int i = 0, s = entities.size(); s > i; i++) {
            int entityId = entities.get(i);
            Entity entity = world.getEntity(entityId);
            String entityIdStr = entityIdMapper.getEntityId(entity);
            if (hasToReplicateToAllClients(entity) || hasToReplicateToClient(entity, clientConnection)) {
                clientConnection.entityAdded(entityIdStr, entity);
                clientTrackingEntities.put(clientConnection, entityIdStr);
            }
        }

        clientConnection.applyChanges();
    }

    private boolean hasToReplicateToAllClients(Entity entity) {
        for (NetworkEntityConfig networkEntityConfig : networkEntityConfigArray) {
            if (networkEntityConfig.isEntitySentToAll(entity))
                return true;
        }
        return false;
    }

    private boolean hasToReplicateToClient(Entity entity, ClientConnection clientConnection) {
        for (NetworkEntityConfig networkEntityConfig : networkEntityConfigArray) {
            if (networkEntityConfig.isEntitySentToClient(entity, clientConnection))
                return true;
        }
        return false;
    }

    private boolean hasToSendToAllClients(EntityEvent entityEvent) {
        for (NetworkEntityConfig networkEntityConfig : networkEntityConfigArray) {
            if (networkEntityConfig.isEventSentToAll(entityEvent))
                return true;
        }
        return false;
    }

    private boolean hasToSendToClient(EntityEvent entityEvent, ClientConnection clientConnection) {
        for (NetworkEntityConfig networkEntityConfig : networkEntityConfigArray) {
            if (networkEntityConfig.isEventSentToClient(entityEvent, clientConnection))
                return true;
        }
        return false;
    }

    private boolean clientTracksEntity(ClientConnection clientConnection, String entityId) {
        return clientTrackingEntities.containsEntry(clientConnection, entityId);
    }

    @Override
    public void removeClientConnection(ClientConnection clientConnection) {
        clientConnection.unsetServerCallback();
        clientTrackingEntities.removeAll(clientConnection);
        clientConnectionMap.remove(clientConnection.getName());
    }

    @Override
    public void disconnectAllClients() {
        for (ClientConnection clientConnection : clientConnectionMap.values()) {
            clientConnection.unsetServerCallback();
            clientConnection.forceDisconnect();
        }
    }

    @Override
    protected void processSystem() {
        broadcastApplyChanges();
    }
}
