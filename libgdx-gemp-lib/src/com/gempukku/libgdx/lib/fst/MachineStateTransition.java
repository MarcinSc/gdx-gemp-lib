package com.gempukku.libgdx.lib.fst;

public interface MachineStateTransition {
    void reset();

    boolean isTriggered();
}
