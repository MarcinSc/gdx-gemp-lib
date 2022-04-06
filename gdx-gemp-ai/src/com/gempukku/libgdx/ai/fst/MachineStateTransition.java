package com.gempukku.libgdx.ai.fst;

public interface MachineStateTransition {
    String getState();

    boolean isTriggered();
}
