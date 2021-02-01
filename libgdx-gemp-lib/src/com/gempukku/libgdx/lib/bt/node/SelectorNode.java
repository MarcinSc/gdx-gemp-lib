package com.gempukku.libgdx.lib.bt.node;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class SelectorNode implements BehaviorNode {
    private Array<BehaviorNode> nodes = new Array<>();
    private int index;
    private boolean nodeStarted;
    private boolean running;

    public void addNode(BehaviorNode behaviorNode) {
        nodes.add(behaviorNode);
    }

    @Override
    public void start() {
        index = 0;
        running = true;
    }

    @Override
    public ProcessResult process(float delta) {
        if (!nodeStarted) {
            nodes.get(index).start();
            nodeStarted = true;
        }
        ProcessResult result = nodes.get(index).process(delta);
        if (result == ProcessResult.Failure) {
            if (index + 1 < nodes.size) {
                index++;
                result = ProcessResult.Continue;
            } else {
                result = ProcessResult.Failure;
            }
        }
        if (result != ProcessResult.Continue) {
            nodes.get(index).finish();
            nodeStarted = false;
        }
        return result;
    }

    @Override
    public void cancel() {
        nodes.get(index).cancel();
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
        return nodes;
    }
}
