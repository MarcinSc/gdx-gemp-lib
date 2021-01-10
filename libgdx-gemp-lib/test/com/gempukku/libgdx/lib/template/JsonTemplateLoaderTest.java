package com.gempukku.libgdx.lib.template;

import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonTemplateLoaderTest extends LibGDXTest {
    @Test
    public void testSimple() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/simple.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        assertEquals("b", result.getString("a"));
    }

    @Test
    public void testArray() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/array.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        JsonValue a = result.get("a");
        assertTrue(a.isArray());
        assertEquals("b", a.get(0).asString());
        assertEquals("c", a.get(1).asString());
    }

    @Test
    public void testExtends() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/extend.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        assertEquals("b", result.getString("a"));
    }

    @Test
    public void testOverrideInFile() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/overrideInFile.json", new ClasspathFileHandleResolver());
        System.out.println(result.toJson(JsonWriter.OutputType.json));
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        assertEquals("c", result.getString("a"));
    }

    @Test
    public void testOverrideInTemplate() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/overrideInTemplate.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        JsonValue a = result.get("a");
        assertTrue(a.isArray());
        assertEquals("b", a.get(0).asString());
        assertEquals("c", a.get(1).asString());
    }

    @Test
    public void testDeepTemplate() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/deepTemplate.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        assertEquals("b", result.getString("a"));
    }

    @Test
    public void testRemoveFields() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/removeFields.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        assertEquals("c", result.getString("b"));
    }

    @Test
    public void testTemplateInside() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/templateInside.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        JsonValue c = result.get("c");
        assertTrue(c.isObject());
        assertEquals(1, c.size);
        JsonValue a = c.get("a");
        assertEquals("b", a.asString());
    }

    @Test
    public void testTemplateInArray() {
        JsonValue result = JsonTemplateLoader.loadTemplateFromFile("template/templateInArray.json", new ClasspathFileHandleResolver());
        assertTrue(result.isObject());
        assertEquals(1, result.size);
        JsonValue c = result.get("c");
        assertTrue(c.isArray());
        JsonValue objectInArray = c.get(0);
        assertTrue(objectInArray.isObject());
        JsonValue a = objectInArray.get("a");
        assertEquals("b", a.asString());
    }
}
