package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.ObjectMap;

public interface ArtemisTriggerState {
    void initializeForWorld(World world);

    void transitioningTo(String newState, Entity entity, ObjectMap<String, Object> blackboard);

    void transitioningFrom(String oldState, Entity entity, ObjectMap<String, Object> blackboard);

    void update(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard);
}
