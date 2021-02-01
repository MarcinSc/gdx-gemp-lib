package com.gempukku.libgdx.lib.fst;

import com.badlogic.gdx.utils.Array;

public class TriggerMachineState implements MachineState {
    private Array<StateTransition> transitions = new Array<>();
    private TriggerState triggerState;

    public TriggerMachineState(TriggerState triggerState) {
        this.triggerState = triggerState;
    }

    @Override
    public String getNextState() {
        for (StateTransition transition : transitions) {
            if (transition.getCondition().isTriggered())
                return transition.getState();
        }

        return null;
    }

    @Override
    public void transitioningTo(String newState) {
        triggerState.transitioningTo(newState);
    }

    @Override
    public void transitioningFrom(String oldState) {
        triggerState.transitioningFrom(oldState);
        for (StateTransition transition : transitions) {
            transition.getCondition().reset();
        }
    }

    @Override
    public void update(float delta) {
        triggerState.update(delta);
    }

    public void addTransition(String state, TriggerCondition transition) {
        transitions.add(new StateTransition(state, transition));
    }

    public void removeTransition(String state, TriggerCondition transition) {
        transitions.removeValue(new StateTransition(state, transition), false);
    }

    private static class StateTransition {
        private String state;
        private TriggerCondition transition;

        public StateTransition(String state, TriggerCondition transition) {
            this.state = state;
            this.transition = transition;
        }

        public String getState() {
            return state;
        }

        public TriggerCondition getCondition() {
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
