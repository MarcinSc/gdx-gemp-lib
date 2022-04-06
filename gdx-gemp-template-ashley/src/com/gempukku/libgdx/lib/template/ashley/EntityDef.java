package com.gempukku.libgdx.lib.template.ashley;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class EntityDef {
    private Array<Component> components;

    public Array<Component> getComponents() {
        return components;
    }
}
