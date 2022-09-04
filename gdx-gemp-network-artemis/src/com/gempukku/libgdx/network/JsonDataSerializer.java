package com.gempukku.libgdx.network;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class JsonDataSerializer implements DataSerializer<JsonValue> {
    private final Json json = new Json();
    private final JsonReader jsonReader = new JsonReader();

    @Override
    public JsonValue serializeData(Object data) {
        return jsonReader.parse(json.toJson(data));
    }

    @Override
    public Object deserializeData(JsonValue data) {
        return json.fromJson(null, data.toJson(JsonWriter.OutputType.json));
    }
}
