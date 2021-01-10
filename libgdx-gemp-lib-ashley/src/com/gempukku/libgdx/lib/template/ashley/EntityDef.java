package com.gempukku.libgdx.lib.template.ashley;

import com.badlogic.ashley.core.Component;

public class EntityDef {
    private Component[] components;

    public Component[] getComponents() {
        return components;
    }

    public void setComponents(Component[] components) {
        this.components = components;
    }
}
