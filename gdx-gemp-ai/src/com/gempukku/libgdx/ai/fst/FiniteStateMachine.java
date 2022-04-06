package com.gempukku.libgdx.ai.fst;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class FiniteStateMachine {
    private String currentState;
    private ObjectMap<String, FiniteMachineState> states = new ObjectMap<>();
    private ObjectSet<String> tmpSet = new ObjectSet<>();

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
        tmpSet.clear();
        FiniteMachineState finiteMachineState = states.get(currentState);
        while (true) {
            tmpSet.add(currentState);
            String newStateName = finiteMachineState.checkTransitions();
            // Can't transition to a state the machine has been in, this frame already
            if (newStateName == null || tmpSet.contains(newStateName))
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
