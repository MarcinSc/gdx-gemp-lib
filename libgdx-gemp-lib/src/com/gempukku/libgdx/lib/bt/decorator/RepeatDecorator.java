package com.gempukku.libgdx.lib.bt.decorator;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class RepeatDecorator extends AbstractBehaviorNodeDecorator {
    private final int count;
    private int runIndex;

    public RepeatDecorator(int count) {
        this.count = count;
    }

    @Override
    public void beforeNodeStarted(BehaviorNode node) {
        runIndex = 0;
    }

    @Override
    public ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result) {
        if (result == ProcessResult.Success) {
            if (runIndex + 1 < count) {
                node.finish();
                node.start();
                runIndex++;
                return ProcessResult.Continue;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Repeat: " + count;
    }
}
