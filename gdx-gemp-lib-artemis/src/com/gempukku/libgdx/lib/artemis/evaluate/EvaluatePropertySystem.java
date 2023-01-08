package com.gempukku.libgdx.lib.artemis.evaluate;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;

public class EvaluatePropertySystem extends BaseSystem {
    private EventSystem eventSystem;
    private Array<PropertyEvaluator> propertyEvaluatorArray = new Array<>();

    public void addPropertyEvaluator(PropertyEvaluator propertyEvaluator) {
        propertyEvaluatorArray.add(propertyEvaluator);
    }

    public void removePropertyEvaluator(PropertyEvaluator propertyEvaluator) {
        propertyEvaluatorArray.removeValue(propertyEvaluator, true);
    }

    @Override
    protected void processSystem() {

    }

    public <T> T evaluateProperty(Entity entity, Object property, Class<T> clazz) {
        if (property instanceof EvaluableProperty) {
            EvaluableProperty evaluableProperty = (EvaluableProperty) property;
            for (PropertyEvaluator propertyEvaluator : propertyEvaluatorArray) {
                if (propertyEvaluator.evaluatesProperty(entity, evaluableProperty))
                    return (T) propertyEvaluator.evaluateValue(entity, evaluableProperty);
            }
        }
        return (T) property;
    }
}
