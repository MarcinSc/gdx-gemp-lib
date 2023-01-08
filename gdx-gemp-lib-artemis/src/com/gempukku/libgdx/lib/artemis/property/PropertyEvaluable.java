package com.gempukku.libgdx.lib.artemis.property;

import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;

public class PropertyEvaluable implements EvaluableProperty {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
