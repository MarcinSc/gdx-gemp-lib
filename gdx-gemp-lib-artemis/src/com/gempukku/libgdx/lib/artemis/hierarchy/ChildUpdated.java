package com.gempukku.libgdx.lib.artemis.hierarchy;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class ChildUpdated implements EntityEvent {
    private int oldParentId;
    private int newParentId;

    public ChildUpdated(int oldParentId, int newParentId) {
        this.oldParentId = oldParentId;
        this.newParentId = newParentId;
    }

    public int getOldParentId() {
        return oldParentId;
    }

    public int getNewParentId() {
        return newParentId;
    }
}
