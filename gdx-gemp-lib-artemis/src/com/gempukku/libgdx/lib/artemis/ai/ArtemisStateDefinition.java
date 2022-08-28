package com.gempukku.libgdx.lib.artemis.ai;

import com.badlogic.gdx.utils.ObjectMap;

public class ArtemisStateDefinition {
    private ArtemisTriggerState state;
    private ObjectMap<String, ArtemisTriggerCondition> conditions;

    public ArtemisTriggerState getState() {
        return state;
    }

    public ObjectMap<String, ArtemisTriggerCondition> getConditions() {
        return conditions;
    }
}
