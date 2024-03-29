package com.gempukku.libgdx.ai.fst;

public interface MachineState {
    String getNextState();

    void transitioningTo(String newState);

    void transitioningFrom(String oldState);

    void update(float delta);
}