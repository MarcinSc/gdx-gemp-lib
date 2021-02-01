package com.gempukku.libgdx.lib.bt.decorator;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class AlwaysFailDecorator extends AbstractBehaviorNodeDecorator {
    @Override
    public ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result) {
        if (result == ProcessResult.Success)
            result = ProcessResult.Failure;
        return result;
    }

    @Override
    public String toString() {
        return "Always fail";
    }
}
