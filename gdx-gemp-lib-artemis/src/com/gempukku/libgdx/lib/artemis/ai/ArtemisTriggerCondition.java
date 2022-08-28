package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.ObjectMap;

public interface ArtemisTriggerCondition {
    void initializeForWorld(World world);

    boolean isTriggered(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard);

    void reset(Entity entity, ObjectMap<String, Object> blackboard);
}
