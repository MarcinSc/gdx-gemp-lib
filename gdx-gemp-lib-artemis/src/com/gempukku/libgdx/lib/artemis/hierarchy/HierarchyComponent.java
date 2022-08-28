package com.gempukku.libgdx.lib.artemis.hierarchy;

import com.artemis.PooledComponent;
import com.artemis.annotations.EntityId;

public class HierarchyComponent extends PooledComponent {
    @EntityId
    private int parentId = -1;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    protected void reset() {
        parentId = -1;
    }
}
