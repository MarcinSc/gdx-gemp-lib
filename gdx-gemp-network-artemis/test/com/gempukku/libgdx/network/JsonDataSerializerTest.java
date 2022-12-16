package com.gempukku.libgdx.network;


import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.JsonValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonDataSerializerTest {
    private JsonDataSerializer dataSerializer;
    private World world;

    @Before
    public void init() {
        dataSerializer = new JsonDataSerializer();
        world = new World();
    }

    @Test
    public void serializeDeserializeEvent() {
        TestEvent source = new TestEvent();
        source.setTestInt(2);
        source.setTestString("test String");
        JsonValue jsonValue = dataSerializer.serializeEvent(source);
        TestEvent result = (TestEvent) dataSerializer.deserializeEntityEvent(jsonValue);
        assertEquals(2, result.getTestInt());
        assertEquals("test String", result.getTestString());
    }

    @Test
    public void serializeDeserializeComponent() {
        TestComponent testComponent = new TestComponent();
        testComponent.setIntField(2);
        testComponent.setStringField("test String");

        JsonValue result = dataSerializer.serializeComponent(testComponent);
        Entity entity = world.createEntity();
        dataSerializer.deserializeComponent(entity, world, result);

        TestComponent resultComponent = world.getMapper(TestComponent.class).get(entity);
        assertEquals(2, resultComponent.getIntField());
        assertEquals("test String", resultComponent.getStringField());
    }

    @Test
    public void serializeDeserializeComponentWithJsonData() {
        JsonValue json = new JsonValue(JsonValue.ValueType.object);
        json.addChild("field", new JsonValue("test"));

        JsonTestComponent jsonTestComponent = new JsonTestComponent();
        jsonTestComponent.setField(json);

        JsonValue result = dataSerializer.serializeComponent(jsonTestComponent);
        Entity entity = world.createEntity();
        dataSerializer.deserializeComponent(entity, world, result);

        JsonTestComponent resultComponent = world.getMapper(JsonTestComponent.class).get(entity);
        assertTrue(resultComponent.getField().type() == JsonValue.ValueType.object);
        assertEquals("test", resultComponent.getField().getString("field"));
    }
}