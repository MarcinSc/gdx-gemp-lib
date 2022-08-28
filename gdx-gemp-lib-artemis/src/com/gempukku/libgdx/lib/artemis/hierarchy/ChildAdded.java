package com.gempukku.libgdx.lib.artemis.hierarchy;


import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class ChildAdded implements EntityEvent {
    private int parentId;

    public ChildAdded(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }
}
