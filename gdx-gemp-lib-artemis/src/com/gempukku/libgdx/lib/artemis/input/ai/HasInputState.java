package com.gempukku.libgdx.lib.artemis.input.ai;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.ai.ArtemisTriggerCondition;
import com.gempukku.libgdx.lib.artemis.input.UserInputStateComponent;

public class HasInputState implements ArtemisTriggerCondition {
    private String state;

    @Override
    public void initializeForWorld(World world) {

    }

    @Override
    public boolean isTriggered(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard) {
        UserInputStateComponent inputState = entity.getComponent(UserInputStateComponent.class);
        return inputState.getStates().contains(state);
    }

    @Override
    public void reset(Entity entity, ObjectMap<String, Object> blackboard) {

    }
}
