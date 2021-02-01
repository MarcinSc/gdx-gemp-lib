package com.gempukku.libgdx.lib.bt.node;

import com.gempukku.libgdx.lib.bt.BehaviorNode;

public abstract class AbstractBehaviorNode implements BehaviorNode {
    private boolean running;

    @Override
    public final void start() {
        running = true;
        nodeStart();
    }

    @Override
    public final void cancel() {
        running = false;
        nodeCancel();
    }

    @Override
    public final void finish() {
        running = false;
        nodeFinish();
    }

    @Override
    public final boolean isRunning() {
        return running;
    }

    protected void nodeStart() {

    }

    protected void nodeCancel() {

    }

    protected void nodeFinish() {

    }
}
