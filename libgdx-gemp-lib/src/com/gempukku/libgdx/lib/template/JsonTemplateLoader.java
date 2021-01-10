package com.gempukku.libgdx.lib.template;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.Arrays;
import java.util.List;

public class JsonTemplateLoader {
    private static final List<String> ACCEPTED_TPL_FIELDS = Arrays.asList("tpl:extends", "tpl:removeFields");

    public static JsonValue loadTemplateFromString(String jsonString, FileHandleResolver resolver) {
        JsonValue originalJson = new JsonReader().parse(jsonString);
        return resolveTemplate(originalJson, resolver);
    }

    public static JsonValue loadTemplateFromFile(String file, FileHandleResolver resolver) {
        return loadTemplateFromFile(resolver.resolve(file), resolver);
    }

    public static JsonValue loadTemplateFromFile(FileHandle fileHandle, FileHandleResolver resolver) {
        JsonValue originalJson = new JsonReader().parse(fileHandle);
        return resolveTemplate(originalJson, resolver);
    }

    private static JsonValue resolveTemplate(JsonValue originalJson, FileHandleResolver resolver) {
        return resolveJson(originalJson, resolver);
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
            appendField(jsonValue, result, removeFields, resolver);
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
        if (!fieldName.startsWith("tpl:") && (removeFields == null || !removeFields.contains(fieldName))) {
            if (result.has(fieldName))
                result.remove(fieldName);
            result.addChild(field.name(), resolveJson(field, resolver));
        }
    }

    private static JsonValue resolveValueForArray(JsonValue json, FileHandleResolver resolver) {
        JsonValue result = new JsonValue(JsonValue.ValueType.array);
        result.setName(json.name());
        JsonValue lastChild = null;
        for (JsonValue jsonValue : json) {
            if (lastChild == null) {
                lastChild = resolveJson(jsonValue, resolver);
                result.addChild(lastChild);
            } else {
                JsonValue newChild = resolveJson(jsonValue, resolver);
                lastChild.next = newChild;
                newChild.parent = result;
                lastChild = newChild;
            }
        }
        result.size = json.size;
        return result;
    }
}
