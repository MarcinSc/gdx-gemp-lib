package com.gempukku.libgdx.ashley.template;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.gempukku.libgdx.ashley.LibGDXTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AshleyTemplateEntityLoaderTest extends LibGDXTest {
    private Engine engine;
    private AshleyEngineJson json;

    @Before
    public void init() {
        engine = new Engine();
        json = new AshleyEngineJson();
        json.setEngine(engine);
    }

    @Test
    public void testSimple() {
        Entity entity = AshleyTemplateEntityLoader.loadTemplateToEngine(engine, "entity/simple.json", json, new ClasspathFileHandleResolver());
        assertEquals(1, entity.getComponents().size());
        assertTrue(entity.getComponents().get(0) instanceof TestComponent);
        TestComponent test = (TestComponent) entity.getComponents().get(0);
        assertEquals("b", test.getA());
    }

    @Test
    public void testMultiple() {
        Entity entity = AshleyTemplateEntityLoader.loadTemplateToEngine(engine, "entity/multiple.json", json, new ClasspathFileHandleResolver());
        assertEquals(2, entity.getComponents().size());
        for (Component component : entity.getComponents()) {
            if (component instanceof TestComponent) {
                assertEquals("b", ((TestComponent) component).getA());
            } else {
                assertEquals("c", ((Test2Component) component).getB());
            }
        }
    }

    @Test
    public void testExtends() {
        Entity entity = AshleyTemplateEntityLoader.loadTemplateToEngine(engine, "entity/extend.json", json, new ClasspathFileHandleResolver());
        assertEquals(2, entity.getComponents().size());
        for (Component component : entity.getComponents()) {
            if (component instanceof TestComponent) {
                assertEquals("b", ((TestComponent) component).getA());
            } else {
                assertEquals("c", ((Test2Component) component).getB());
            }
        }
    }
}
