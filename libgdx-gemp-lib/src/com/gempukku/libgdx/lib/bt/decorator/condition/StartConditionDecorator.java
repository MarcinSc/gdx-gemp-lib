package com.gempukku.libgdx.lib.bt.decorator.condition;

import com.badlogic.gdx.utils.Predicate;
import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;
import com.gempukku.libgdx.lib.bt.decorator.AbstractBehaviorNodeDecorator;

public class StartConditionDecorator extends AbstractBehaviorNodeDecorator {
    private Predicate<BehaviorNode> predicate;
    private boolean startSuccessful;

    public StartConditionDecorator(Predicate<BehaviorNode> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void beforeNodeStarted(BehaviorNode node) {
        startSuccessful = predicate.evaluate(node);
    }

    @Override
    public ProcessResult beforeNodeProcessed(BehaviorNode node, float delta) {
        if (!startSuccessful)
            return ProcessResult.Failure;
        return null;
    }

    @Override
    public String toString() {
        return "Start condition: " + predicate.toString();
    }
}
