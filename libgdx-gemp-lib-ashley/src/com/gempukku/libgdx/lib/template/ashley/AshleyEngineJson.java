package com.gempukku.libgdx.lib.template.ashley;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class AshleyEngineJson extends Json {
    private Engine engine;

    public AshleyEngineJson(Engine engine) {
        this.engine = engine;
    }

    public AshleyEngineJson(JsonWriter.OutputType outputType, Engine engine) {
        super(outputType);
        this.engine = engine;
    }

    @Override
    protected Object newInstance(Class type) {
        if (ClassReflection.isAssignableFrom(Component.class, type))
            return engine.createComponent(type);
        return super.newInstance(type);
    }
}
