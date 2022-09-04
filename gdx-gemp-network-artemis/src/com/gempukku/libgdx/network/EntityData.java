package com.gempukku.libgdx.network;

import java.util.LinkedList;
import java.util.List;

public class EntityData<T> {
    private List<T> componentData = new LinkedList<>();

    public void addComponentData(T componentData) {
        this.componentData.add(componentData);
    }

    public List<T> getComponentData() {
        return componentData;
    }
}
