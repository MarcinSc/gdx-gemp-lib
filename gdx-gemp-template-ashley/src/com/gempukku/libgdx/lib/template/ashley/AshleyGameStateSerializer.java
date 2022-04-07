package com.gempukku.libgdx.lib.template.ashley;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.template.JsonTemplateLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class AshleyGameStateSerializer {
    private static final AshleyEngineJson json = new AshleyEngineJson();
    private static final JsonReader reader = new JsonReader();

    public static void loadIntoEngine(Engine engine, String filePath, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(filePath, resolver);
        loadJsonTemplateToEngine(engine, value);
    }

    public static void loadIntoEngine(Engine engine, Reader reader, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(reader, resolver);
        loadJsonTemplateToEngine(engine, value);
    }

    private static void loadJsonTemplateToEngine(Engine engine, JsonValue value) {
        json.setEngine(engine);

        JsonValue entities = value.get("entities");
        for (JsonValue jsonEntity : entities) {
            EntityDef entityDef = AshleyTemplateEntityLoader.convertToAshley(jsonEntity, json);

            Entity entity = engine.createEntity();
            for (Component component : entityDef.getComponents()) {
                entity.add(component);
            }
            engine.addEntity(entity);
        }
    }

    private static JsonValue convertComponentToJson(Component component) {
        return reader.parse(json.toJson(component));
    }

    public static void saveFromEngine(Engine engine, FileHandle fileHandle) {
        try {
            Writer writer = fileHandle.writer(false);
            try {
                saveFromEngine(engine, writer);
            } finally {
                writer.close();
            }
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to save to disk", exp);
        }
    }

    public static void saveFromEngine(Engine engine, Writer writer) {
        json.setEngine(engine);

        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        Array<Entity> entityArray = new Array<>();

        for (Entity entity : engine.getEntities()) {
            InternalEntityComponent internalEntity = entity.getComponent(InternalEntityComponent.class);
            if (internalEntity == null) {
                entityArray.add(entity);
            }
        }

        JsonValue entitiesJson = JsonUtils.convertToJsonArray(entityArray, new JsonUtils.JsonConverter<Entity>() {
            @Override
            public JsonValue convert(Entity entity) {
                Array<Component> componentArray = new Array<>();
                for (Component component : entity.getComponents()) {
                    if (component.getClass().getAnnotation(InternalComponent.class) == null) {
                        componentArray.add(component);
                    }
                }

                JsonValue result = new JsonValue(JsonValue.ValueType.object);
                for (Component component : componentArray) {
                    result.addChild(component.getClass().getName(), convertComponentToJson(component));
                }
                return result;
            }
        });

        result.addChild("entities", entitiesJson);

        try {
            writer.write(result.toJson(JsonWriter.OutputType.json));
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to save to disk", exp);
        }
    }
}
