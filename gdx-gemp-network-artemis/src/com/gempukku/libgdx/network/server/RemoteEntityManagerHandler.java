package com.gempukku.libgdx.network.server;

import com.artemis.*;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.event.RawEventListener;
import com.gempukku.libgdx.network.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;

public class RemoteEntityManagerHandler extends BaseSystem implements RemoteHandler {
    private Map<String, ClientConnection> clientConnectionMap = new HashMap<>();
    private Multimap<ClientConnection, Integer> clientTrackingEntities = HashMultimap.create();

    private Bag<Component> tempComponentBag = new Bag<>();

    private EventSystem eventSystem;
    private EntitySubscription allEntitiesSubscription;

    @Override
    public void initialize() {
        allEntitiesSubscription = world.getAspectSubscriptionManager().get(Aspect.all());
        allEntitiesSubscription.addSubscriptionListener(
                new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) {

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
                        if (event.getClass().getAnnotation(SendToClients.class) != null) {
                            for (ClientConnection clientConnection : clientConnectionMap.values()) {
                                if (clientTracksEntity(clientConnection, entity.getId()))
                                    clientConnection.eventSent(entity.getId(), event);
                            }
                        }
                    }
                });
    }

    @EventListener
    public void entityUpdated(EntityUpdated entityUpdated, Entity entity) {
        loadComponentBag(entity);

        boolean replicateToAllClients = hasToReplicateToAllClients(tempComponentBag);

        int entityId = entity.getId();
        for (Map.Entry<String, ClientConnection> clientConnectionEntry : clientConnectionMap.entrySet()) {
            String clientName = clientConnectionEntry.getKey();
            ClientConnection clientConnection = clientConnectionEntry.getValue();
            if (replicateToAllClients || hasToReplicateToClient(tempComponentBag, clientName)) {
                if (clientTracksEntity(clientConnection, entityId)) {
                    clientConnection.entityModified(entity);
                } else {
                    clientTrackingEntities.put(clientConnection, entityId);
                    clientConnection.entityAdded(entity);
                }
            } else if (clientTracksEntity(clientConnection, entityId)) {
                clientConnection.entityRemoved(entityId);
                clientTrackingEntities.remove(clientConnection, entityId);
            }
        }
    }

    private void loadComponentBag(Entity entity) {
        tempComponentBag.clear();
        entity.getComponents(tempComponentBag);
    }

    private static boolean hasToReplicateToAllClients(Bag<Component> componentBag) {
        boolean replicateToAllClients = false;
        for (Component component : componentBag) {
            if (component.getClass().getAnnotation(ReplicateToClients.class) != null) {
                replicateToAllClients = true;
                break;
            }
        }
        return replicateToAllClients;
    }

    private void entityRemoved(int entityId) {
        for (ClientConnection clientConnection : clientConnectionMap.values()) {
            if (clientTracksEntity(clientConnection, entityId)) {
                clientConnection.entityRemoved(entityId);
                clientTrackingEntities.remove(clientConnection, entityId);
            }
        }
    }

    private void broadcastApplyChanges() {
        for (ClientConnection value : clientConnectionMap.values()) {
            value.applyChanges();
        }
    }

    private static boolean hasToReplicateToClient(Bag<Component> componentBag, final String username) {
        for (Component component : componentBag) {
            Class<? extends Component> componentClass = component.getClass();
            if (componentClass.getAnnotation(ReplicateToOwner.class) != null) {
                if (component instanceof OwnedComponent && ((OwnedComponent) component).getOwner().equals(username))
                    return true;
                if (component instanceof OwnedByMultipleComponent && ((OwnedByMultipleComponent) component).getOwners().contains(username))
                    return true;
            }
        }
        return false;
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
                    public void processEvent(String fromUser, int entityId, EventFromClient event) {
                        Entity trackedEntity = findTrackedEntity(entityId);
                        if (trackedEntity != null) {
                            event.setOrigin(fromUser);
                            eventSystem.fireEvent(event, trackedEntity);
                        }
                    }

                    private Entity findTrackedEntity(int entityId) {
                        if (clientTracksEntity(clientConnection, entityId))
                            return world.getEntity(entityId);
                        return null;
                    }
                });

        IntBag entities = allEntitiesSubscription.getEntities();
        for (int i = 0, s = entities.size(); s > i; i++) {
            Entity entity = world.getEntity(entities.get(i));
            loadComponentBag(entity);
            if (hasToReplicateToAllClients(tempComponentBag) || hasToReplicateToClient(tempComponentBag, clientName)) {
                clientConnection.entityAdded(entity);
            }
        }

        clientConnection.applyChanges();
    }

    private boolean clientTracksEntity(ClientConnection clientConnection, int entityId) {
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
