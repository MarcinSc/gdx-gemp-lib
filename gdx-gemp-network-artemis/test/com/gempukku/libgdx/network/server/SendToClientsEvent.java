package com.gempukku.libgdx.network.server;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;
import com.gempukku.libgdx.network.server.config.annotation.SendToClients;

@SendToClients
public class SendToClientsEvent implements EntityEvent {
}
