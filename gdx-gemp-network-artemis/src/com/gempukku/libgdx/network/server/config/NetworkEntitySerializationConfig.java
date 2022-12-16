package com.gempukku.libgdx.network.server.config;

import com.artemis.Component;
import com.gempukku.libgdx.network.server.ClientConnection;

public interface NetworkEntitySerializationConfig {
     boolean isComponentSerializedToClient(Component component, ClientConnection clientConnection);
}
