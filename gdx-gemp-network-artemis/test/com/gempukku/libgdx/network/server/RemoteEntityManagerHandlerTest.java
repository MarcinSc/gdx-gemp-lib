package com.gempukku.libgdx.network.server;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.gempukku.libgdx.lib.artemis.event.EntityEventDispatcher;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.network.EntityUpdated;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;


public class RemoteEntityManagerHandlerTest {
    private World world;
    private RemoteEntityManagerHandler remoteEntityManagerHandler;
    private EventSystem eventSystem;

    @Before
    public void setup() {
        remoteEntityManagerHandler = new RemoteEntityManagerHandler();

        WorldConfigurationBuilder worldConfigurationBuilder = new WorldConfigurationBuilder();
        eventSystem = new EventSystem(Mockito.mock(EntityEventDispatcher.class));
        worldConfigurationBuilder.with(
                eventSystem,
                remoteEntityManagerHandler);
        world = new World(worldConfigurationBuilder.build());
    }

    @Test
    public void existingEntitiesSentToAllConnectingClientsIfReplicateToClients() {
        Entity entity = world.createEntity();
        world.getMapper(SendToAllComponent.class).create(entity);
        remoteEntityManagerHandler.processSystem();

        world.process();

        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);
        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).entityAdded(Mockito.any(Entity.class));
        Mockito.verify(clientConnection).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void existingEntitiesNotSentToConnectingClientIfNotReplicate() {
        Entity entity = world.createEntity();

        world.process();

        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);
        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void existingEntitiesSentToConnectingClientIfReplicateToOwner() {
        Entity entity = world.createEntity();
        SendToOwnerComponent component = world.getMapper(SendToOwnerComponent.class).create(entity);
        component.setOwner("user1");

        world.process();

        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);
        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).entityAdded(Mockito.any(Entity.class));
        Mockito.verify(clientConnection).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void existingEntitiesNotSentToConnectingClientIfReplicateToOwner() {
        Entity entity = world.createEntity();
        SendToOwnerComponent component = world.getMapper(SendToOwnerComponent.class).create(entity);
        component.setOwner("user2");

        world.process();

        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);
        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    private ClientConnection mockClientConnection(String username) {
        ClientConnection clientConnection = Mockito.mock(ClientConnection.class);
        Mockito.when(clientConnection.getName()).thenReturn(username);
        return clientConnection;
    }

    @Test
    public void newEntitiesSentToClientIfReplicateToClients() {
        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);

        Entity entity = world.createEntity();
        world.getMapper(SendToAllComponent.class).create(entity);

        remoteEntityManagerHandler.entityUpdated(new EntityUpdated(), entity);

        world.process();

        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).entityAdded(Mockito.any(Entity.class));
        Mockito.verify(clientConnection, new Times(2)).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void newEntitiesNotSentToClientIfNotReplicate() {
        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);

        Entity entity = world.createEntity();

        remoteEntityManagerHandler.entityUpdated(new EntityUpdated(), entity);

        world.process();

        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection, new Times(2)).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void newEntitiesSentToClientIfReplicateToOwner() {
        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);

        Entity entity = world.createEntity();
        SendToOwnerComponent component = world.getMapper(SendToOwnerComponent.class).create(entity);
        component.setOwner("user1");

        remoteEntityManagerHandler.entityUpdated(new EntityUpdated(), entity);

        world.process();

        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).entityAdded(Mockito.any(Entity.class));
        Mockito.verify(clientConnection, new Times(2)).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void newEntitiesNotSentToClientIfReplicateToOwner() {
        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);

        Entity entity = world.createEntity();
        SendToOwnerComponent component = world.getMapper(SendToOwnerComponent.class).create(entity);
        component.setOwner("user2");

        remoteEntityManagerHandler.entityUpdated(new EntityUpdated(), entity);

        world.process();

        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection, new Times(2)).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void eventSentToClientOnObservedEntity() {
        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);

        Entity entity = world.createEntity();
        SendToOwnerComponent component = world.getMapper(SendToOwnerComponent.class).create(entity);
        component.setOwner("user1");

        remoteEntityManagerHandler.entityUpdated(new EntityUpdated(), entity);

        SendToClientsEvent event = new SendToClientsEvent();
        eventSystem.fireEvent(event, entity);

        world.process();

        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection).entityAdded(Mockito.any(Entity.class));
        Mockito.verify(clientConnection).eventSent(Mockito.anyInt(), Mockito.eq(event));
        Mockito.verify(clientConnection, new Times(2)).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }

    @Test
    public void eventNotSentToClientOnNotObservedEntity() {
        ClientConnection clientConnection = mockClientConnection("user1");

        remoteEntityManagerHandler.addClientConnection(clientConnection);

        Entity entity = world.createEntity();
        SendToOwnerComponent component = world.getMapper(SendToOwnerComponent.class).create(entity);
        component.setOwner("user2");

        remoteEntityManagerHandler.entityUpdated(new EntityUpdated(), entity);

        SendToClientsEvent event = new SendToClientsEvent();
        eventSystem.fireEvent(event, entity);

        world.process();

        Mockito.verify(clientConnection).getName();
        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
        Mockito.verify(clientConnection, new Times(2)).applyChanges();
        Mockito.verifyNoMoreInteractions(clientConnection);
    }
//
//    @Test
//    public void entityRemovedWhenComponentRemoved() {
//        ClientConnection clientConnection = Mockito.mock(ClientConnection.class);
//
//        remoteEntityManagerHandler.addClientConnection("user1", clientConnection);
//
//        EntityRef entity = defaultEntityManager.createEntity();
//        entity.createComponent(SendToAllComponent.class);
//        entity.saveChanges();
//
//        entity.removeComponents(SendToAllComponent.class);
//        entity.saveChanges();
//
//        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
//        Mockito.verify(clientConnection).entityAdded(Mockito.any(String.class), Mockito.any(EntityData.class));
//        Mockito.verify(clientConnection).entityRemoved(Mockito.any(String.class));
//        Mockito.verify(clientConnection).applyChanges();
//        Mockito.verifyNoMoreInteractions(clientConnection);
//    }
//
//    @Test
//    public void entityRemovedWhenOwnerChanged() {
//        ClientConnection clientConnection = Mockito.mock(ClientConnection.class);
//
//        remoteEntityManagerHandler.addClientConnection("user1", clientConnection);
//
//        EntityRef entity = defaultEntityManager.createEntity();
//        SendToOwnerComponent component = entity.createComponent(SendToOwnerComponent.class);
//        component.setOwner("user1");
//        entity.saveChanges();
//
//        component = entity.getComponent(SendToOwnerComponent.class);
//        component.setOwner("user2");
//        entity.saveChanges();
//
//        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
//        Mockito.verify(clientConnection).entityAdded(Mockito.any(String.class), Mockito.any(EntityData.class));
//        Mockito.verify(clientConnection).entityRemoved(Mockito.any(String.class));
//        Mockito.verify(clientConnection).applyChanges();
//        Mockito.verifyNoMoreInteractions(clientConnection);
//    }
//
//    @Test
//    public void entityRemovedWhenEntityDestroyed() {
//        ClientConnection clientConnection = Mockito.mock(ClientConnection.class);
//
//        remoteEntityManagerHandler.addClientConnection("user1", clientConnection);
//
//        EntityRef entity = defaultEntityManager.createEntity();
//        entity.createComponent(SendToAllComponent.class);
//        entity.saveChanges();
//
//        defaultEntityManager.destroyEntity(entity);
//
//        Mockito.verify(clientConnection).setServerCallback(Mockito.any(ServerCallback.class));
//        Mockito.verify(clientConnection).entityAdded(Mockito.any(String.class), Mockito.any(EntityData.class));
//        Mockito.verify(clientConnection).entityRemoved(Mockito.any(String.class));
//        Mockito.verify(clientConnection).applyChanges();
//        Mockito.verifyNoMoreInteractions(clientConnection);
//    }
}