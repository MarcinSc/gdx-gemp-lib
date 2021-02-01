package com.gempukku.libgdx.lib.bt.decorator;

import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.BehaviorNodeDecorator;
import com.gempukku.libgdx.lib.bt.ProcessResult;

public class AbstractBehaviorNodeDecorator implements BehaviorNodeDecorator {
    @Override
    public void beforeNodeStarted(BehaviorNode node) {

    }

    @Override
    public ProcessResult beforeNodeProcessed(BehaviorNode node, float delta) {
        return null;
    }

    @Override
    public ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result) {
        return null;
    }

    @Override
    public void beforeNodeCancelled(BehaviorNode node) {

    }

    @Override
    public void afterNodeFinished(BehaviorNode node) {

    }
}
