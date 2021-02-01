package com.gempukku.libgdx.lib.bt;

import com.badlogic.gdx.utils.Array;

public class DecoratedBehaviorNode implements BehaviorNode {
    private BehaviorNode node;
    private Array<BehaviorNodeDecorator> decorators = new Array<>();

    public DecoratedBehaviorNode(BehaviorNode node) {
        this.node = node;
    }

    public void addDecorator(BehaviorNodeDecorator decorator) {
        decorators.add(decorator);
    }

    @Override
    public void start() {
        for (BehaviorNodeDecorator decorator : decorators) {
            decorator.beforeNodeStarted(node);
        }
        node.start();
    }

    @Override
    public ProcessResult process(float delta) {
        ProcessResult result = node.process(delta);
        for (BehaviorNodeDecorator decorator : decorators) {
            ProcessResult decoratorResult = decorator.afterNodeProcessed(node, delta, result);
            if (decoratorResult != null)
                result = decoratorResult;
        }
        return result;
    }

    @Override
    public void cancel() {
        for (BehaviorNodeDecorator decorator : decorators) {
            decorator.beforeNodeCancelled(node);
        }
        node.cancel();
    }

    @Override
    public void finish() {
        node.finish();
        for (BehaviorNodeDecorator decorator : decorators) {
            decorator.afterNodeFinished(node);
        }
    }

    @Override
    public boolean isRunning() {
        return node.isRunning();
    }

    @Override
    public Iterable<BehaviorNode> getChildren() {
        return node.getChildren();
    }
}
