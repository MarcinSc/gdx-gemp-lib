package com.gempukku.libgdx.ashley.template;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Json;

public class AshleyEngineJson extends Json {
    private Engine engine;

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    protected Object newInstance(Class type) {
        if (Component.class.isAssignableFrom(type))
            return engine.createComponent((Class<Component>) type);

        return super.newInstance(type);
    }
}
