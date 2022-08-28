package com.gempukku.libgdx.lib.artemis.event;

import com.badlogic.gdx.utils.ObjectMap;

public class PropertiesPrioritySource implements PrioritySource {
    private ObjectMap<String, String> properties;

    public PropertiesPrioritySource(ObjectMap<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public float getPriority(String priorityName) {
        String value = properties.get(priorityName);
        if (value == null)
            return 0f;
        return Float.parseFloat(value);
    }
}
