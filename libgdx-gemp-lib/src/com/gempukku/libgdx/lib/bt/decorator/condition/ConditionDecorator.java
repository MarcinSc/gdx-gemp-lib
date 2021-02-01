package com.gempukku.libgdx.lib.bt.decorator.condition;

import com.badlogic.gdx.utils.Predicate;
import com.gempukku.libgdx.lib.bt.BehaviorNode;
import com.gempukku.libgdx.lib.bt.ProcessResult;
import com.gempukku.libgdx.lib.bt.decorator.AbstractBehaviorNodeDecorator;

public class ConditionDecorator extends AbstractBehaviorNodeDecorator {
    private Predicate<BehaviorNode> predicate;

    public ConditionDecorator(Predicate<BehaviorNode> predicate) {
        this.predicate = predicate;
    }

    @Override
    public ProcessResult beforeNodeProcessed(BehaviorNode node, float delta) {
        if (!predicate.evaluate(node))
            return ProcessResult.Failure;
        return null;
    }

    @Override
    public String toString() {
        return "Condition: " + predicate.toString();
    }
}
