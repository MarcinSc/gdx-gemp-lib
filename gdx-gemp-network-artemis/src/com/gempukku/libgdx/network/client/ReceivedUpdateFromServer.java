package com.gempukku.libgdx.network.client;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

import java.util.List;

public class ReceivedUpdateFromServer<T> implements EntityEvent {
    private List<IncomingInformationPacket<T>> packets;

    public ReceivedUpdateFromServer(List<IncomingInformationPacket<T>> packets) {
        this.packets = packets;
    }

    public List<IncomingInformationPacket<T>> getPackets() {
        return packets;
    }
}
