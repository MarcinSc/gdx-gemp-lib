package com.gempukku.libgdx.template;

import com.badlogic.gdx.utils.JsonValue;

public class JsonUtils {
    public static <T> JsonValue convertToJsonArray(Iterable<T> t, JsonConverter<T> converter) {
        JsonValue resultArray = new JsonValue(JsonValue.ValueType.array);
        JsonValue lastChild = null;
        int count = 0;
        for (T value : t) {
            JsonValue convertedValue = converter.convert(value);
            if (lastChild == null) {
                lastChild = convertedValue;
                resultArray.addChild(lastChild);
            } else {
                lastChild.next = convertedValue;
                lastChild = convertedValue;
            }
            count++;
        }
        resultArray.size = count;

        return resultArray;
    }

    public interface JsonConverter<T> {
        JsonValue convert(T value);
    }
}
