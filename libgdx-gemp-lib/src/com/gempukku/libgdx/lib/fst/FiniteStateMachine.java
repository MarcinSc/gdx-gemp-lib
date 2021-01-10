package com.gempukku.libgdx.lib.fst;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class FiniteStateMachine {
    private String currentState;
    private ObjectMap<String, FiniteMachineState> states = new ObjectMap<>();

    public FiniteStateMachine(String initialState, MachineState machineState) {
        this.currentState = initialState;
        addState(initialState, machineState);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void addState(String state, MachineState machineState) {
        if (states.containsKey(state))
            throw new IllegalArgumentException("State already defined: " + state);
        states.put(state, new FiniteMachineState(machineState));
    }

    public void addStateTransition(String state, MachineStateTransition transition) {
        FiniteMachineState machineState = states.get(state);
        if (machineState == null)
            throw new IllegalArgumentException("State not found: " + state);
        machineState.addStateTransition(transition);
    }

    public void update(float delta) {
        FiniteMachineState finiteMachineState = states.get(currentState);
        while (true) {
            String newStateName = finiteMachineState.checkTransitions();
            if (newStateName == null)
                break;
            finiteMachineState.getMachineState().transitioningTo(newStateName);
            finiteMachineState = states.get(newStateName);
            finiteMachineState.getMachineState().transitioningFrom(currentState);
            currentState = newStateName;
        }
        finiteMachineState.getMachineState().update(delta);
    }

    private static class FiniteMachineState {
        private MachineState machineState;
        private Array<MachineStateTransition> transitions = new Array<>();

        public FiniteMachineState(MachineState machineState) {
            this.machineState = machineState;
        }

        public MachineState getMachineState() {
            return machineState;
        }

        public void addStateTransition(MachineStateTransition transition) {
            transitions.add(transition);
        }

        public String checkTransitions() {
            for (MachineStateTransition transition : transitions) {
                if (transition.isTriggered())
                    return transition.getState();
            }
            return null;
        }
    }
}
