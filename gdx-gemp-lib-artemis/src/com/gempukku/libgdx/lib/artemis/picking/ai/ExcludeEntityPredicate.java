package com.gempukku.libgdx.lib.artemis.picking.ai;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Predicate;

class ExcludeEntityPredicate implements Predicate<Entity> {
    private Integer entityId;

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean evaluate(Entity entity) {
        return entityId == null || entity.getId() != entityId;
    }
}
