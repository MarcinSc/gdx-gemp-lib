package com.gempukku.libgdx.network.server;

import com.gempukku.libgdx.network.EventFromClient;

public interface ServerCallback {
    void processEvent(String fromUser, String entityId, EventFromClient event);
}
