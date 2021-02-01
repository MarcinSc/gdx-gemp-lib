package com.gempukku.libgdx.lib.bt.node;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

import java.util.BitSet;

public class ParallelNode implements BehaviorNode {
    private Array<BehaviorNode> nodes = new Array<>();
    private Policy policy;
    private BitSet statusBitSet;
    private boolean running;

    public ParallelNode(Policy policy) {
        this.policy = policy;
    }

    public void addNode(BehaviorNode behaviorNode) {
        nodes.add(behaviorNode);
    }

    @Override
    public void start() {
        statusBitSet = new BitSet(nodes.size);
        for (int i = 0; i < nodes.size; i++) {
            nodes.get(i).start();
            statusBitSet.set(i);
        }
        running = true;
    }

    @Override
    public ProcessResult process(float delta) {
        for (int i = 0; i < nodes.size; i++) {
            ProcessResult result = nodes.get(i).process(delta);
            if (result != ProcessResult.Continue) {
                nodes.get(i).finish();
                statusBitSet.clear(i);
            }
            if (policy.finished(result)) {
                cancelRemainingNodes();
                return result;
            }
        }
        if (statusBitSet.isEmpty())
            return policy.getEmptyResult();

        return ProcessResult.Continue;
    }

    @Override
    public void cancel() {
        cancelRemainingNodes();
        statusBitSet = null;
        running = false;
    }

    @Override
    public void finish() {
        statusBitSet = null;
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

    private void cancelRemainingNodes() {
        for (int i = 0; i < statusBitSet.size(); i++) {
            if (statusBitSet.get(i)) {
                nodes.get(i).cancel();
            }
        }
    }

    public enum Policy {
        Sequence, Selector;

        public boolean finished(ProcessResult result) {
            switch (this) {
                case Sequence:
                    return result == ProcessResult.Failure;
                case Selector:
                    return result == ProcessResult.Success;
            }
            throw new IllegalStateException();
        }

        public ProcessResult getEmptyResult() {
            switch (this) {
                case Sequence:
                    return ProcessResult.Success;
                case Selector:
                    return ProcessResult.Failure;
            }
            throw new IllegalStateException();
        }
    }
}
