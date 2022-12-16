package com.github.czyzby.websocket.impl;

import com.github.czyzby.websocket.data.WebSocketCloseCode;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.data.WebSocketState;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Default web socket implementation for desktop and mobile platforms.
 *
 * @author MJ
 */
public class NvWebSocket extends AbstractWebSocket {
    private final WebSocketFactory webSocketFactory = new WebSocketFactory();
    private WebSocket webSocket;
    private Map<String, String> headers = new HashMap<String, String>();

    public NvWebSocket(final String url) {
        super(url);
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void connect() throws WebSocketException {
        try {
            dispose();
            final WebSocket currentWebSocket = webSocket = webSocketFactory.createSocket(getUrl());
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                currentWebSocket.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
            currentWebSocket.addListener(new NvWebSocketListener(this));
            currentWebSocket.connect();
        } catch (final Throwable exception) {
            throw new WebSocketException("Unable to connect.", exception);
        }
    }

    /**
     * Removes current web socket instance.
     */
    protected void dispose() {
        final WebSocket currentWebSocket = webSocket;
        if (currentWebSocket != null && currentWebSocket.isOpen()) {
            try {
                currentWebSocket.disconnect(WebSocketCloseCode.AWAY.getCode());
            } catch (final Exception exception) {
                postErrorEvent(exception);
            }
        }
    }

    @Override
    public WebSocketState getState() {
        final WebSocket currentWebSocket = webSocket;
        return currentWebSocket == null ? WebSocketState.CLOSED : convertState(currentWebSocket.getState());
    }

    private static WebSocketState convertState(final com.neovisionaries.ws.client.WebSocketState state) {
        switch (state) {
            case CLOSED:
            case CREATED:
                return WebSocketState.CLOSED;
            case CLOSING:
                return WebSocketState.CLOSING;
            case CONNECTING:
                return WebSocketState.CONNECTING;
            case OPEN:
                return WebSocketState.OPEN;
        }
        return WebSocketState.CLOSED;
    }

    @Override
    public boolean isSecure() {
        final WebSocket currentWebSocket = webSocket;
        return currentWebSocket != null && "wss".equalsIgnoreCase(currentWebSocket.getURI().getScheme());
    }

    @Override
    public boolean isOpen() {
        final WebSocket currentWebSocket = webSocket;
        return currentWebSocket != null && currentWebSocket.isOpen();
    }

    @Override
    public void close(final WebSocketCloseCode code, final String reason) throws WebSocketException {
        final WebSocket currentWebSocket = webSocket;
        if (currentWebSocket != null) {
            try {
                currentWebSocket.disconnect(code.getCode(), reason);
            } catch (final Throwable exception) {
                throw new WebSocketException("Unable to close the web socket.", exception);
            }
        }
    }

    @Override
    protected void sendBinary(final byte[] packet) throws Exception {
        webSocket.sendBinary(packet);
    }

    @Override
    protected void sendString(final String packet) throws Exception {
        webSocket.sendText(packet);
    }
}
