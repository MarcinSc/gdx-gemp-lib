package com.gempukku.libgdx.artemis.template;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.template.JsonTemplateLoader;

public class ArtemisTemplateEntityLoader {
    private static final ArtemisWorldJson json = new ArtemisWorldJson();

    static {
        json.setSerializer(JsonValue.class,
                new Json.ReadOnlySerializer<JsonValue>() {
                    @Override
                    public JsonValue read(Json json, JsonValue jsonData, Class type) {
                        return jsonData;
                    }
                });
    }

    public static Entity loadTemplateToWorld(World world, String file, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(file, resolver);
        return loadTemplateToWorld(world, jsonValue);
    }

    public static Entity loadTemplateToWorld(World world, FileHandle fileHandle, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(fileHandle, resolver);
        return loadTemplateToWorld(world, jsonValue);
    }

    public static Entity loadTemplateToWorld(World world, JsonValue jsonValue) {
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("ArtemisTemplate", jsonValue.toJson(JsonWriter.OutputType.json));

        Entity entity = world.createEntity();
        return loadFragmentToEntity(world, entity, jsonValue);
    }

    public static Entity loadFragmentToEntity(World world, Entity entity, FileHandle fileHandle, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(fileHandle, resolver);
        return loadFragmentToEntity(world, entity, jsonValue);
    }

    public static Entity loadFragmentToEntity(World world, Entity entity, String file, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(file, resolver);
        return loadFragmentToEntity(world, entity, jsonValue);
    }

    public static Entity loadFragmentToEntity(World world, Entity entity, JsonValue jsonValue) {
        json.setEntity(entity);
        json.setWorld(world);

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
