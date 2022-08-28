package com.gempukku.libgdx.lib.artemis.ai.condition;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.ai.ArtemisTriggerCondition;

public class OrCondition implements ArtemisTriggerCondition {
    private Array<ArtemisTriggerCondition> conditions;

    @Override
    public void initializeForWorld(World world) {
        for (ArtemisTriggerCondition condition : conditions) {
            condition.initializeForWorld(world);
        }
    }

    @Override
    public boolean isTriggered(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard) {
        for (ArtemisTriggerCondition condition : conditions) {
            if (condition.isTriggered(deltaTime, entity, blackboard))
                return true;
        }
        return false;
    }

    @Override
    public void reset(Entity entity, ObjectMap<String, Object> blackboard) {
        for (ArtemisTriggerCondition condition : conditions) {
            condition.reset(entity, blackboard);
        }
    }
}
