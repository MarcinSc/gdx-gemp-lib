package com.gempukku.libgdx.lib.artemis.selection;

import com.artemis.Entity;
import com.artemis.PooledComponent;

public class SelectTargetComponent extends PooledComponent {
    private Entity targetEntity;

    public Entity getTargetEntity() {
        return targetEntity;
    }

    public void setTargetEntity(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Override
    protected void reset() {
        targetEntity = null;
    }
}
