package com.gempukku.libgdx.template.ashley;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class EntityDef {
    private Array<Component> components;

    public void setComponents(Array<Component> components) {
        this.components = components;
    }

    public Array<Component> getComponents() {
        return components;
    }
}
