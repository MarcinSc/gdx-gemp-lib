package com.gempukku.libgdx.lib.fst;

public interface MachineStateTransition {
    String getState();

    boolean isTriggered();
}
