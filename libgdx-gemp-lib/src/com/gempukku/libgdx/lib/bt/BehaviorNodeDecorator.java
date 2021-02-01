package com.gempukku.libgdx.lib.bt;

public interface BehaviorNodeDecorator {
    void beforeNodeStarted(BehaviorNode node);

    ProcessResult beforeNodeProcessed(BehaviorNode node, float delta);

    ProcessResult afterNodeProcessed(BehaviorNode node, float delta, ProcessResult result);

    void beforeNodeCancelled(BehaviorNode node);

    void afterNodeFinished(BehaviorNode node);
}
