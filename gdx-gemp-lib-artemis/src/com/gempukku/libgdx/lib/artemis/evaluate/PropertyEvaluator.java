package com.gempukku.libgdx.lib.artemis.evaluate;

import com.artemis.Entity;

public interface PropertyEvaluator {
    boolean evaluatesProperty(Entity entity, EvaluableProperty value);

    Object evaluateValue(Entity entity, EvaluableProperty value);
}
