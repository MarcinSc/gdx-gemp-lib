package com.gempukku.libgdx.network.client;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.event.RawEventListener;
import com.gempukku.libgdx.network.DataSerializer;
import com.gempukku.libgdx.network.NetworkMessage;
import com.gempukku.libgdx.network.NetworkMessageMarshaller;
import com.gempukku.libgdx.network.SendToServer;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.impl.NvWebSocket;
import com.github.czyzby.websocket.net.ExtendedNet;

import java.util.LinkedList;
import java.util.List;

public class WebsocketRemoteClientConnector<T> extends BaseSystem {
    private final LinkedList<List<IncomingInformationPacket<T>>> readyToProcessMessages = new LinkedList<>();

    private WebSocket socket;
    private ServerSession<T> serverSession;

    private boolean disconnected;

    private DataSerializer<T> dataSerializer;
    private ServerSessionProducer<T> serverSessionProducer;
    private NetworkMessageMarshaller<T> networkMessageMarshaller;
    private Entity serverConnectionEntity;
    private EventSystem eventSystem;

    public WebsocketRemoteClientConnector(DataSerializer<T> dataSerializer, ServerSessionProducer<T> serverSessionProducer,
                                          NetworkMessageMarshaller<T> networkMessageMarshaller) {
        this.dataSerializer = dataSerializer;
        this.serverSessionProducer = serverSessionProducer;
        this.networkMessageMarshaller = networkMessageMarshaller;
    }

    @Override
    public void initialize() {
        eventSystem.addRawEventListener(
                new RawEventListener() {
                    @Override
                    public void eventDispatched(EntityEvent event, Entity entity) {
                        if (entity != null && entity.getComponent(ServerEntityComponent.class) != null
                                && event.getClass().getAnnotation(SendToServer.class) != null) {
                            sendEventToServer(event, entity.getComponent(ServerEntityComponent.class).getEntityId());
                        }
                    }
                });
        serverConnectionEntity = world.createEntity();
    }

    private void sendEventToServer(EntityEvent event, String entityId) {
        NetworkMessage<T> message = new NetworkMessage<T>(entityId, NetworkMessage.Type.EVENT, dataSerializer.serializeEvent(event));

        serverSession.sendMessage(message);
    }

    @Override
    protected void processSystem() {
        synchronized (readyToProcessMessages) {
            if (!readyToProcessMessages.isEmpty()) {
                eventSystem.fireEvent(new ReceivedUpdateFromServer<T>(readyToProcessMessages.removeFirst()), serverConnectionEntity);
            }
        }
        if (disconnected) {
            eventSystem.fireEvent(new ConnectionLost(), serverConnectionEntity);
            disconnected = false;
        }
    }

    private IncomingInformationPacket<T> convertEventPayload(NetworkMessage<T> networkMessage) {
        try {
            return IncomingInformationPacket.event(networkMessage.getEntityId(), dataSerializer.deserializeEntityEvent(networkMessage.getPayloadList().get(0)));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    private IncomingInformationPacket<T> convertCreatePayload(NetworkMessage<T> networkMessage) {
        try {
            return IncomingInformationPacket.create(networkMessage.getEntityId(), networkMessage.getPayloadList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IncomingInformationPacket<T> convertModifyPayload(NetworkMessage<T> networkMessage) {
        try {
            return IncomingInformationPacket.update(networkMessage.getEntityId(), networkMessage.getPayloadList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IncomingInformationPacket<T> convertDestroyPayload(NetworkMessage<T> networkMessage) {
        return IncomingInformationPacket.destroy(networkMessage.getEntityId());
    }

    public void connectToServer(String host, int port, String address, String authenticationToken) {
        if (socket != null)
            throw new IllegalStateException();

        socket = ExtendedNet.getNet().newWebSocket(host, port, address);
        socket.addListener(createListener());
        socket.setSerializeAsString(true);
        if (authenticationToken != null)
            ((NvWebSocket) socket).addHeader("Authorization", authenticationToken);
        socket.connect();

        serverSession = serverSessionProducer.createServerSession(socket);
    }

    private WebSocketListener createListener() {
        return new AbstractWebSocketListener() {
            private final LinkedList<IncomingInformationPacket<T>> networkMessages = new LinkedList<>();

            @Override
            public boolean onOpen(final WebSocket webSocket) {
                return FULLY_HANDLED;
            }

            @Override
            protected boolean onMessage(WebSocket webSocket, Object packet) throws WebSocketException {
                try {
                    NetworkMessage<T> networkMessage = networkMessageMarshaller.unmarshall((T) packet);
                    if (networkMessage.getType() == NetworkMessage.Type.APPLY_CHANGES) {
                        synchronized (readyToProcessMessages) {
                            readyToProcessMessages.add(new LinkedList<>(networkMessages));
                        }
                        networkMessages.clear();
                    } else {
                        IncomingInformationPacket<T> incomingInformationPacket = null;
                        if (networkMessage.getType() == NetworkMessage.Type.EVENT)
                            incomingInformationPacket = convertEventPayload(networkMessage);
                        else if (networkMessage.getType() == NetworkMessage.Type.ENTITY_CREATED)
                            incomingInformationPacket = convertCreatePayload(networkMessage);
                        else if (networkMessage.getType() == NetworkMessage.Type.ENTITY_MODIFIED)
                            incomingInformationPacket = convertModifyPayload(networkMessage);
                        else if (networkMessage.getType() == NetworkMessage.Type.ENTITY_REMOVED)
                            incomingInformationPacket = convertDestroyPayload(networkMessage);

                        if (incomingInformationPacket == null)
                            throw new Exception("Unable to read server packet");

                        networkMessages.add(incomingInformationPacket);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                    socket.close();
                    socket = null;
                }
                return FULLY_HANDLED;
            }

            @Override
            public boolean onClose(final WebSocket webSocket, final WebSocketCloseCode code, final String reason) {
                disconnected = true;
                return FULLY_HANDLED;
            }
        };
    }

    public void disconnectFromServer() {
        if (socket == null)
            throw new IllegalStateException();

        WebSockets.closeGracefully(socket);
        socket = null;
    }
}
