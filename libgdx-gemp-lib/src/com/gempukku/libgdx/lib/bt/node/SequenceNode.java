package com.gempukku.libgdx.lib.bt.node;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class SequenceNode implements BehaviorNode {
    private Array<BehaviorNode> nodes = new Array<>();
    private int index;
    private boolean running;

    public void addNode(BehaviorNode behaviorNode) {
        nodes.add(behaviorNode);
    }

    @Override
    public void start() {
        index = 0;
        nodes.get(index).start();
        running = true;
    }

    @Override
    public ProcessResult process(float delta) {
        ProcessResult result = nodes.get(index).process(delta);
        if (result == ProcessResult.Success) {
            nodes.get(index).finish();
            if (index + 1 < nodes.size) {
                index++;
                nodes.get(index).start();
                result = ProcessResult.Continue;
            } else {
                result = ProcessResult.Success;
            }
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
        nodes.get(index).finish();
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
