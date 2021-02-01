package com.gempukku.libgdx.lib.bt.node;

import com.gempukku.libgdx.lib.bt.ProcessResult;

public class WaitNode extends AbstractBehaviorNode {
    private float time;
    private float accumulatedTime;

    public WaitNode(float time) {
        this.time = time;
    }

    @Override
    public void nodeStart() {
        accumulatedTime = 0;
    }

    @Override
    public ProcessResult process(float delta) {
        accumulatedTime += delta;
        return (accumulatedTime >= time) ? ProcessResult.Success : ProcessResult.Continue;
    }
}
