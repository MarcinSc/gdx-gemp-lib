package com.gempukku.libgdx.lib.artemis.property;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.evaluate.PropertyEvaluator;

public class PropertySystem extends BaseSystem implements PropertyEvaluator {
    private ObjectMap<String, String> properties;
    private EvaluatePropertySystem evaluatePropertySystem;

    public PropertySystem(ObjectMap<String, String> properties) {
        this.properties = properties;
    }

    @Override
    protected void initialize() {
        evaluatePropertySystem.addPropertyEvaluator(this);
    }

    public String getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public boolean evaluatesProperty(Entity entity, EvaluableProperty value) {
        return value instanceof PropertyEvaluable;
    }

    @Override
    public Object evaluateValue(Entity entity, EvaluableProperty value) {
        return properties.get(((PropertyEvaluable) value).getName());
    }

    @Override
    protected void processSystem() {

    }
}
