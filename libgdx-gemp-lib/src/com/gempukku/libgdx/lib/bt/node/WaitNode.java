package com.gempukku.libgdx.lib.bt.node;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class WaitNode implements BehaviorNode {
    private float time;
    private float accumulatedTime;
    private boolean running;

    public WaitNode(float time) {
        this.time = time;
    }

    @Override
    public void start() {
        accumulatedTime = 0;
        running = true;
    }

    @Override
    public ProcessResult process(float delta) {
        accumulatedTime += delta;
        return (accumulatedTime >= time) ? ProcessResult.Success : ProcessResult.Continue;
    }

    @Override
    public void cancel() {
        running = false;
    }

    @Override
    public void finish() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Iterable<BehaviorNode> getChildren() {
        return null;
    }
}
