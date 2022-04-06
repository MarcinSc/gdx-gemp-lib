package com.gempukku.libgdx.ai.fst;

public interface TriggerState {
    void transitioningTo(String newState);

    void transitioningFrom(String oldState);

    void update(float delta);
}