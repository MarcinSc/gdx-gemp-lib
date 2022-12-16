package com.gempukku.libgdx.network.client;

import com.artemis.Component;

public class ServerEntityComponent extends Component {
    private String entityId;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
