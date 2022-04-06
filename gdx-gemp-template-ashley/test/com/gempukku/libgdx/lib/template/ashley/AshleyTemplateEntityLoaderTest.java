package com.gempukku.libgdx.lib.template.ashley;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.utils.Json;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AshleyTemplateEntityLoaderTest extends LibGDXTest {
    @Test
    public void testSimple() {
        EntityDef entityDef = AshleyTemplateEntityLoader.loadTemplate("entity/simple.json", new Json(), new ClasspathFileHandleResolver());
        assertEquals(1, entityDef.getComponents().size);
        assertTrue(entityDef.getComponents().get(0) instanceof TestComponent);
        TestComponent test = (TestComponent) entityDef.getComponents().get(0);
        assertEquals("b", test.getA());
    }

    @Test
    public void testMultiple() {
        EntityDef entityDef = AshleyTemplateEntityLoader.loadTemplate("entity/multiple.json", new Json(), new ClasspathFileHandleResolver());
        assertEquals(2, entityDef.getComponents().size);
        for (Component component : entityDef.getComponents()) {
            if (component instanceof TestComponent) {
                assertEquals("b", ((TestComponent) component).getA());
            } else {
                assertEquals("c", ((Test2Component) component).getB());
            }
        }
    }

    @Test
    public void testExtends() {
        EntityDef entityDef = AshleyTemplateEntityLoader.loadTemplate("entity/extend.json", new Json(), new ClasspathFileHandleResolver());
        assertEquals(2, entityDef.getComponents().size);
        for (Component component : entityDef.getComponents()) {
            if (component instanceof TestComponent) {
                assertEquals("b", ((TestComponent) component).getA());
            } else {
                assertEquals("c", ((Test2Component) component).getB());
            }
        }
    }
}
