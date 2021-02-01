package com.gempukku.libgdx.lib.bt.node;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ContainerBehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class SelectorNode extends AbstractBehaviorNode implements ContainerBehaviorNode {
    private Array<BehaviorNode> nodes = new Array<>();
    private int index;

    public void addNode(BehaviorNode behaviorNode) {
        nodes.add(behaviorNode);
    }

    @Override
    public void nodeStart() {
        index = 0;
        nodes.get(index).start();
    }

    @Override
    public ProcessResult process(float delta) {
        ProcessResult result = nodes.get(index).process(delta);
        if (result == ProcessResult.Failure) {
            if (index + 1 < nodes.size) {
                nodes.get(index).finish();
                index++;
                nodes.get(index).start();
                result = ProcessResult.Continue;
            } else {
                result = ProcessResult.Failure;
            }
        }
        return result;
    }

    @Override
    public void nodeCancel() {
        nodes.get(index).cancel();
    }

    @Override
    public void nodeFinish() {
        nodes.get(index).finish();
    }

    @Override
    public Iterable<BehaviorNode> getChildren() {
        return nodes;
    }
}
