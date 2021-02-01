package com.gempukku.libgdx.lib.bt.decorator;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class RepeatUntilSuccessDecorator extends AbstractBehaviorNodeDecorator {
    @Override
    public ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result) {
        if (result == ProcessResult.Failure) {
            node.finish();
            node.start();
            return ProcessResult.Continue;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Repeat until success";
    }
}
