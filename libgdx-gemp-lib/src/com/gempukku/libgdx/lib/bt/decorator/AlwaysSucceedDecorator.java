package com.gempukku.libgdx.lib.bt.decorator;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class AlwaysSucceedDecorator extends AbstractBehaviorNodeDecorator {
    @Override
    public ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result) {
        if (result == ProcessResult.Failure)
            result = ProcessResult.Success;
        return result;
    }

    @Override
    public String toString() {
        return "Always succeed";
    }
}
