package com.gempukku.libgdx.artemis.template;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.gempukku.libgdx.template.JsonTemplateLoader;
import com.gempukku.libgdx.template.JsonUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ArtemisGameStateSerializer {
    private static final ArtemisWorldJson json = new ArtemisWorldJson();
    private static final JsonReader reader = new JsonReader();

    public static void loadIntoEngine(World world, String filePath, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(filePath, resolver);
        loadJsonTemplateToEngine(world, value);
    }

    public static void loadIntoEngine(World world, Reader reader, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(reader, resolver);
        loadJsonTemplateToEngine(world, value);
    }

    private static void loadJsonTemplateToEngine(World world, JsonValue value) {
        JsonValue entities = value.get("entities");
        for (JsonValue jsonEntity : entities) {
            ArtemisTemplateEntityLoader.loadArtemisTemplateToWorld(world, jsonEntity);
        }
    }

    private static JsonValue convertComponentToJson(Component component) {
        return reader.parse(json.toJson(component));
    }

    public static void saveFromEngine(World world, FileHandle fileHandle) {
        try {
            Writer writer = fileHandle.writer(false);
            try {
                saveFromEngine(world, writer);
            } finally {
                writer.close();
            }
        } catch (IOException exp) {
            throw new GdxRuntimeException("Unable to save to disk", exp);
        }
    }

    public static void saveFromEngine(World world, Writer writer) {
        json.setWorld(world);
        json.setEntity(null);

        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        Array<Entity> entityArray = new Array<>();

        IntBag entityIds = world.getAspectSubscriptionManager().get(Aspect.exclude(InternalEntityComponent.class)).getEntities();
        for (int i = 0, s = entityIds.size(); s > i; i++) {
            Entity entity = world.getEntity(entityIds.get(i));
            InternalEntityComponent internalEntity = entity.getComponent(InternalEntityComponent.class);
            if (internalEntity == null) {
                entityArray.add(entity);
            }
        }

        final Bag<Component> componentBag = new Bag<>();

        JsonValue entitiesJson = JsonUtils.convertToJsonArray(entityArray, new JsonUtils.JsonConverter<Entity>() {
            @Override
            public JsonValue convert(Entity entity) {
                componentBag.clear();
                Array<Component> componentArray = new Array<>();
                for (Component component : entity.getComponents(componentBag)) {
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
