package com.gempukku.libgdx.template;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

public class JsonTemplateLoader {
    private static final List<String> ACCEPTED_TPL_FIELDS = Arrays.asList("tpl:extends", "tpl:removeFields", "tpl:comment");

    public static JsonValue loadTemplateFromString(String jsonString, FileHandleResolver resolver) {
        JsonValue originalJson = new JsonReader().parse(jsonString);
        return resolveTemplate(originalJson, resolver);
    }

    public static JsonValue loadTemplateFromFile(String file, FileHandleResolver resolver) {
        return loadTemplateFromFile(resolver.resolve(file), resolver);
    }

    public static JsonValue loadTemplateFromFile(Reader reader, FileHandleResolver resolver) {
        JsonValue originalJson = new JsonReader().parse(reader);
        return resolveTemplate(originalJson, resolver);
    }

    public static JsonValue loadTemplateFromFile(FileHandle fileHandle, FileHandleResolver resolver) {
        JsonValue originalJson = new JsonReader().parse(fileHandle);
        return resolveTemplate(originalJson, resolver);
    }

    private static JsonValue resolveTemplate(JsonValue originalJson, FileHandleResolver resolver) {
        JsonValue jsonValue = resolveJson(originalJson, resolver);
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("JsonTemplate", jsonValue.toJson(JsonWriter.OutputType.json));
        return jsonValue;
    }

    private static JsonValue resolveJson(JsonValue json, FileHandleResolver resolver) {
        if (json.type() == JsonValue.ValueType.object) {
            return resolveValueForObject(json, resolver);
        } else if (json.type() == JsonValue.ValueType.array) {
            return resolveValueForArray(json, resolver);
        } else if (json.type() == JsonValue.ValueType.booleanValue) {
            return new JsonValue(json.asBoolean());
        } else if (json.type() == JsonValue.ValueType.doubleValue) {
            return new JsonValue(json.asDouble());
        } else if (json.type() == JsonValue.ValueType.longValue) {
            return new JsonValue(json.asLong());
        } else if (json.type() == JsonValue.ValueType.stringValue) {
            return new JsonValue(json.asString());
        } else if (json.type() == JsonValue.ValueType.nullValue) {
            return new JsonValue(JsonValue.ValueType.nullValue);
        }
        throw new GdxRuntimeException("Unknown type of Json type");
    }

    private static JsonValue resolveValueForObject(JsonValue json, FileHandleResolver resolver) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        ObjectSet<String> removeFields = null;
        for (JsonValue jsonValue : json) {
            String name = jsonValue.name();
            if (name.startsWith("tpl:") && !ACCEPTED_TPL_FIELDS.contains(name))
                throw new GdxRuntimeException("Unknown template field: " + name + ", accepted fields: " + ACCEPTED_TPL_FIELDS.toString());
        }

        if (json.has("tpl:removeFields")) {
            if (!json.has("tpl:extends"))
                throw new GdxRuntimeException("tpl:removeFields can only be used in combination with tpl:extends");

            JsonValue jsonValue = json.get("tpl:removeFields");
            if (jsonValue.isString()) {
                removeFields = new ObjectSet<>();
                removeFields.add(jsonValue.asString());
            } else if (jsonValue.isArray()) {
                removeFields = new ObjectSet<>();
                removeFields.addAll(jsonValue.asStringArray());
            } else {
                throw new GdxRuntimeException("tpl:removeFields is neither a String or an array of Strings");
            }
        }
        if (json.has("tpl:extends")) {
            JsonValue jsonValue = json.get("tpl:extends");
            if (jsonValue.isString()) {
                appendTemplateWithFilter(jsonValue.asString(), result, removeFields, resolver);
            } else if (jsonValue.isArray()) {
                for (String template : jsonValue.asStringArray()) {
                    appendTemplateWithFilter(template, result, removeFields, resolver);
                }
            }
        }
        for (JsonValue jsonValue : json) {
            String fieldName = jsonValue.name();
            if (!fieldName.startsWith("tpl:")) {
                appendField(jsonValue, result, removeFields, resolver);
            }
        }

        return result;
    }

    private static void appendTemplateWithFilter(String template, JsonValue result, ObjectSet<String> removeFields, FileHandleResolver resolver) {
        for (JsonValue value : JsonTemplateLoader.loadTemplateFromFile(template, resolver)) {
            appendField(value, result, removeFields, resolver);
        }
    }

    private static void appendField(JsonValue field, JsonValue result, ObjectSet<String> removeFields, FileHandleResolver resolver) {
        String fieldName = field.name();
        if (removeFields == null || !removeFields.contains(fieldName)) {
            if (result.has(fieldName)) {
                JsonValue existingChild = result.get(fieldName);
                if (existingChild.isObject() && field.isObject()) {
                    mergeIn(field, existingChild);
                } else {
                    result.remove(fieldName);
                    result.addChild(fieldName, resolveJson(field, resolver));
                }
            } else {
                result.addChild(fieldName, resolveJson(field, resolver));
            }
        }
    }

    private static void mergeIn(JsonValue merge, JsonValue into) {
        for (JsonValue fieldInObject : merge) {
            String fieldName = fieldInObject.name();
            if (into.has(fieldName)) {
                JsonValue existingChild = into.get(fieldName);
                if (existingChild.isObject() && fieldInObject.isObject()) {
                    mergeIn(fieldInObject, existingChild);
                } else {
                    into.remove(fieldName);
                    into.addChild(fieldName, resolveJson(fieldInObject, null));
                }
            } else {
                into.addChild(fieldName, resolveJson(fieldInObject, null));
            }
        }
    }

    private static JsonValue resolveValueForArray(JsonValue json, FileHandleResolver resolver) {
        JsonValue result = new JsonValue(JsonValue.ValueType.array);
        for (JsonValue child : json) {
            result.addChild(resolveJson(child, resolver));
        }
        return result;
    }
}
