package com.gempukku.libgdx.network;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class JsonDataSerializer implements DataSerializer<JsonValue> {
    private final Json json = new Json();
    private final JsonReader jsonReader = new JsonReader();

    public JsonDataSerializer() {
        json.setUsePrototypes(false);
        json.setSerializer(JsonValue.class,
                new Json.Serializer<JsonValue>() {
                    @Override
                    public void write(Json json, JsonValue object, Class knownType) {
                        json.writeValue(object.toJson(JsonWriter.OutputType.json));
                    }

                    @Override
                    public JsonValue read(Json json, JsonValue jsonData, Class type) {
                        String result = jsonData.asString();
                        if (result == null)
                            return null;
                        return jsonReader.parse(result);
                    }
                }
        );
    }

    @Override
    public JsonValue serializeEvent(EntityEvent entityEvent) {
        String jsonText = json.toJson(entityEvent, (Class<?>) null);
        return jsonReader.parse(jsonText);
    }

    @Override
    public JsonValue serializeComponent(Component component) {
        String jsonText = this.json.toJson(component, (Class<?>) null);
        return jsonReader.parse(jsonText);
    }

    @Override
    public EntityEvent deserializeEntityEvent(JsonValue data) {
        return json.fromJson(null, data.toJson(JsonWriter.OutputType.json));
    }

    @Override
    public Component deserializeComponent(Entity entity, World world, JsonValue data) {
        try {
            Class<? extends Component> componentClass = (Class<? extends Component>) Class.forName(data.getString("class"));
            ComponentMapper<? extends Component> mapper = world.getMapper(componentClass);
            Component component = mapper.create(entity);
            for (JsonValue child : data) {
                String name = child.name();
                if (!name.equals("class")) {
                    this.json.readField(component, name, data);
                }
            }
            return component;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
