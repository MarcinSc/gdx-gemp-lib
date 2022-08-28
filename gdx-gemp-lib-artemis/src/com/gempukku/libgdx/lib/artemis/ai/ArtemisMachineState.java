package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectMap;

public interface ArtemisMachineState {
    String getNextState(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard);

    void transitioningTo(String newState, Entity entity, ObjectMap<String, Object> blackboard);

    void transitioningFrom(String oldState, Entity entity, ObjectMap<String, Object> blackboard);

    void update(float delta, Entity entity, ObjectMap<String, Object> blackboard);
}
