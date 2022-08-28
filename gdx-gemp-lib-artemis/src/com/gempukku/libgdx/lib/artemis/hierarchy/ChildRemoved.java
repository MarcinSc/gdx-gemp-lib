package com.gempukku.libgdx.lib.artemis.hierarchy;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class ChildRemoved implements EntityEvent {
    private int parentId;

    public ChildRemoved(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }
}
