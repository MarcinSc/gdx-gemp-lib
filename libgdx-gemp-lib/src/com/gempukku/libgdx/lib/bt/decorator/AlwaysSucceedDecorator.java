package com.gempukku.libgdx.lib.bt.decorator;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.BehaviorNodeDecorator;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class AlwaysSucceedDecorator implements BehaviorNodeDecorator {
    @Override
    public void beforeNodeStarted(BehaviorNode node) {

    }

    @Override
    public ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result) {
        if (result == ProcessResult.Failure)
            result = ProcessResult.Success;
        return result;
    }

    @Override
    public void beforeNodeCancelled(BehaviorNode node) {

    }

    @Override
    public void afterNodeFinished(BehaviorNode node) {

    }

    @Override
    public String toString() {
        return "Always succeed";
    }
}
