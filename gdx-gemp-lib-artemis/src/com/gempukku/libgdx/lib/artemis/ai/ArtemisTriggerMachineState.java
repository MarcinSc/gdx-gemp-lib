package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ArtemisTriggerMachineState implements ArtemisMachineState {
    private final Array<StateTransition> transitions = new Array<>();
    private final ArtemisTriggerState triggerState;

    public ArtemisTriggerMachineState(ArtemisTriggerState triggerState) {
        this.triggerState = triggerState;
    }

    @Override
    public String getNextState(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard) {
        for (StateTransition transition : transitions) {
            if (transition.getCondition().isTriggered(deltaTime, entity, blackboard))
                return transition.getState();
        }

        return null;
    }

    @Override
    public void transitioningTo(String newState, Entity entity, ObjectMap<String, Object> blackboard) {
        triggerState.transitioningTo(newState, entity, blackboard);
        for (StateTransition transition : transitions) {
            transition.getCondition().reset(entity, blackboard);
        }
    }

    @Override
    public void transitioningFrom(String oldState, Entity entity, ObjectMap<String, Object> blackboard) {
        triggerState.transitioningFrom(oldState, entity, blackboard);
    }

    @Override
    public void update(float delta, Entity entity, ObjectMap<String, Object> blackboard) {
        triggerState.update(delta, entity, blackboard);
    }

    public void addTransition(String state, ArtemisTriggerCondition transition) {
        transitions.add(new StateTransition(state, transition));
    }

    public void removeTransition(String state, ArtemisTriggerCondition transition) {
        transitions.removeValue(new StateTransition(state, transition), false);
    }

    private static class StateTransition {
        private String state;
        private ArtemisTriggerCondition transition;

        public StateTransition(String state, ArtemisTriggerCondition transition) {
            this.state = state;
            this.transition = transition;
        }

        public String getState() {
            return state;
        }

        public ArtemisTriggerCondition getCondition() {
            return transition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StateTransition that = (StateTransition) o;

            if (!state.equals(that.state)) return false;
            return transition.equals(that.transition);
        }

        @Override
        public int hashCode() {
            int result = state.hashCode();
            result = 31 * result + transition.hashCode();
            return result;
        }
    }
}