package com.gempukku.libgdx.lib.bt;

public interface BehaviorNode {
    void start();

    ProcessResult process(float delta);

    void cancel();

    void finish();

    boolean isRunning();
}
