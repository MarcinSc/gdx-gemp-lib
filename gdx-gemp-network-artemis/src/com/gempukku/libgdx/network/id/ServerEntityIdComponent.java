package com.gempukku.libgdx.network.id;

import com.artemis.PooledComponent;

public class ServerEntityIdComponent extends PooledComponent {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected void reset() {
        id = null;
    }
}
