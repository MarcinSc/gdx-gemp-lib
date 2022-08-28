package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class ArtemisFiniteStateMachine {
    private final String initialState;
    private final ObjectMap<String, ArtemisMachineState> states = new ObjectMap<>();
    private final ObjectSet<String> tmpSet = new ObjectSet<>();

    public ArtemisFiniteStateMachine(String initialState) {
        this.initialState = initialState;
    }

    public void addState(String state, ArtemisMachineState machineState) {
        if (states.containsKey(state))
            throw new IllegalArgumentException("State already defined: " + state);
        states.put(state, machineState);
    }

    public String update(float delta, String currentState, Entity entity, ObjectMap<String, Object> blackboard) {
        if (currentState == null)
            currentState = initialState;
        tmpSet.clear();
        ArtemisMachineState finiteMachineState = states.get(currentState);
        while (true) {
            tmpSet.add(currentState);
            String newStateName = finiteMachineState.getNextState(delta, entity, blackboard);
            // Can't transition to a state the machine has been in, this frame already
            if (newStateName == null || tmpSet.contains(newStateName))
                break;
            if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
                Gdx.app.debug("FSM", "Transitioning to: " + newStateName);
            finiteMachineState.transitioningTo(newStateName, entity, blackboard);
            finiteMachineState = states.get(newStateName);
            finiteMachineState.transitioningFrom(currentState, entity, blackboard);
            currentState = newStateName;
        }
        finiteMachineState.update(delta, entity, blackboard);
        return currentState;
    }
}
