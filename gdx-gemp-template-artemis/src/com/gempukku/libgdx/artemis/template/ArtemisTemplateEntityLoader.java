package com.gempukku.libgdx.artemis.template;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.template.JsonTemplateLoader;

public class ArtemisTemplateEntityLoader {
    public static Entity loadTemplateToWorld(World world, String file, ArtemisWorldJson json, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(file, resolver);
        return loadAshleyTempalteToWorld(world, jsonValue, json);
    }

    public static Entity loadTemplateToWorld(World world, FileHandle fileHandle, ArtemisWorldJson json, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(fileHandle, resolver);
        return loadAshleyTempalteToWorld(world, jsonValue, json);
    }

    public static Entity loadAshleyTempalteToWorld(World world, JsonValue jsonValue, ArtemisWorldJson json) {
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("AshleyTemplate", jsonValue.toJson(JsonWriter.OutputType.json));

        Entity entity = world.createEntity();
        json.setEntity(entity);

        try {
            for (JsonValue component : jsonValue) {
                String componentClazzName = component.name();
                Class<? extends Component> componentClass = (Class<? extends Component>) Class.forName(componentClazzName);
                json.readValue(componentClass, component);
            }
        } catch (ClassNotFoundException exp) {
            throw new GdxRuntimeException("Unable to find class for component", exp);
        }

        return entity;
    }
}
