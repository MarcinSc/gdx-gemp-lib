package com.gempukku.libgdx.network;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public abstract class EventFromClient implements EntityEvent {
    private String origin;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
