package com.gempukku.libgdx.lib.bt;

import com.badlogic.gdx.utils.Array;

public class DecoratedBehaviorNode implements ContainerBehaviorNode {
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
        ProcessResult result = null;

        for (BehaviorNodeDecorator decorator : decorators) {
            ProcessResult decoratorResult = decorator.beforeNodeProcessed(node, delta);
            if (decoratorResult != null) {
                result = decoratorResult;
            }
        }

        if (result == null) {
            result = node.process(delta);
            for (BehaviorNodeDecorator decorator : decorators) {
                ProcessResult decoratorResult = decorator.afterNodeProcessed(node, delta, result);
                if (decoratorResult != null)
                    result = decoratorResult;
            }
        } else {
            if (result != ProcessResult.Continue) {
                cancel();
            }
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
        if (node instanceof ContainerBehaviorNode)
            return ((ContainerBehaviorNode) node).getChildren();
        return null;
    }
}
