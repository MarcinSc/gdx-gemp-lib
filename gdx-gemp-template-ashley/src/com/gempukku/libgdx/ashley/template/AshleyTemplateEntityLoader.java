package com.gempukku.libgdx.ashley.template;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.template.JsonTemplateLoader;

public class AshleyTemplateEntityLoader {
    public static Entity loadTemplateToEngine(Engine engine, String file, AshleyEngineJson json, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(file, resolver);
        return loadAshleyTemplateToEngine(engine, jsonValue, json);
    }

    public static Entity loadTemplateToEngine(Engine engine, FileHandle fileHandle, AshleyEngineJson json, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(fileHandle, resolver);
        return loadAshleyTemplateToEngine(engine, jsonValue, json);
    }

    public static Entity loadAshleyTemplateToEngine(Engine engine, JsonValue jsonValue, AshleyEngineJson json) {
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("AshleyTemplate", jsonValue.toJson(JsonWriter.OutputType.json));

        Entity entity = engine.createEntity();

        try {
            for (JsonValue component : jsonValue) {
                String componentClazzName = component.name();
                Class<? extends Component> componentClass = (Class<? extends Component>) Class.forName(componentClazzName);
                json.readValue(componentClass, component);
                entity.add(json.readValue(componentClass, component));
            }
        } catch (ClassNotFoundException exp) {
            throw new GdxRuntimeException("Unable to find class for component", exp);
        }
        engine.addEntity(entity);

        return entity;
    }
}
