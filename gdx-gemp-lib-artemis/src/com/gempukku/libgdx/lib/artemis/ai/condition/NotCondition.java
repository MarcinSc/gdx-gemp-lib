package com.gempukku.libgdx.lib.artemis.ai.condition;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.ai.ArtemisTriggerCondition;

public class NotCondition implements ArtemisTriggerCondition {
    private ArtemisTriggerCondition condition;

    @Override
    public void initializeForWorld(World world) {
        condition.initializeForWorld(world);
    }

    @Override
    public boolean isTriggered(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard) {
        return !condition.isTriggered(deltaTime, entity, blackboard);
    }

    @Override
    public void reset(Entity entity, ObjectMap<String, Object> blackboard) {
        condition.reset(entity, blackboard);
    }
}
