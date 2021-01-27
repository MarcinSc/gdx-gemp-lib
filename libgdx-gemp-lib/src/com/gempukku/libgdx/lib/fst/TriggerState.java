package com.gempukku.libgdx.lib.fst;

public interface TriggerState {
    void transitioningTo(String newState);

    void transitioningFrom(String oldState);

    void update(float delta);
}
