package com.gempukku.libgdx.ai.fst;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class FiniteStateMachine {
    private String currentState;
    private ObjectMap<String, MachineState> states = new ObjectMap<>();
    private ObjectSet<String> tmpSet = new ObjectSet<>();

    public FiniteStateMachine(String initialState) {
        this.currentState = initialState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void addState(String state, MachineState machineState) {
        if (states.containsKey(state))
            throw new IllegalArgumentException("State already defined: " + state);
        states.put(state, machineState);
    }

    public void update(float delta) {
        tmpSet.clear();
        MachineState finiteMachineState = states.get(currentState);
        while (true) {
            tmpSet.add(currentState);
            String newStateName = finiteMachineState.getNextState();
            // Can't transition to a state the machine has been in, this frame already
            if (newStateName == null || tmpSet.contains(newStateName))
                break;
            if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
                Gdx.app.debug("FSM", "Transitioning to: " + newStateName);
            finiteMachineState.transitioningTo(newStateName);
            finiteMachineState = states.get(newStateName);
            finiteMachineState.transitioningFrom(currentState);
            currentState = newStateName;
        }
        finiteMachineState.update(delta);
    }
}