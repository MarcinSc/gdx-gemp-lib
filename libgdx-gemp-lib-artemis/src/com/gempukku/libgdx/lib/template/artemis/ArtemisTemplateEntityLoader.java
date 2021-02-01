package com.gempukku.libgdx.lib.template.artemis;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.lib.template.JsonTemplateLoader;

public class ArtemisTemplateEntityLoader {
    public static EntityDef loadTemplate(String file, Json json, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(file, resolver);
        return convertToArtemis(jsonValue, json);
    }

    public static EntityDef loadTemplate(FileHandle fileHandle, Json json, FileHandleResolver resolver) {
        JsonValue jsonValue = JsonTemplateLoader.loadTemplateFromFile(fileHandle, resolver);
        return convertToArtemis(jsonValue, json);
    }

    public static EntityDef convertToArtemis(JsonValue jsonValue, Json json) {
        JsonValue artemisJson = convertToArtemisEntityJson(jsonValue);
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("ArtemisTemplate", artemisJson.toJson(JsonWriter.OutputType.json));
        return json.readValue(EntityDef.class, artemisJson);
    }

    public static JsonValue convertToArtemisEntityJson(JsonValue value) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        JsonValue componentsArray = new JsonValue(JsonValue.ValueType.array);
        JsonValue lastChild = null;
        for (JsonValue jsonValue : value) {
            String name = jsonValue.name();
            jsonValue.addChild("class", new JsonValue(name));
            if (lastChild == null) {
                lastChild = jsonValue;
                componentsArray.addChild(lastChild);
            } else {
                lastChild.next = jsonValue;
                jsonValue.parent = result;
                lastChild = jsonValue;
            }
        }
        componentsArray.size = value.size;
        result.addChild("components", componentsArray);
        return result;
    }
}
