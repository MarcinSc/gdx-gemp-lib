package com.gempukku.libgdx.artemis.template;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.gempukku.libgdx.artemis.LibGDXTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArtemisTemplateEntityLoaderTest extends LibGDXTest {
    private World world;

    @Before
    public void init() {
        world = new World();
    }

    @Test
    public void testSimple() {
        Entity entity = ArtemisTemplateEntityLoader.loadTemplateToWorld(world, "entity/simple.json", new ClasspathFileHandleResolver());
        assertEquals(1, entity.getComponents(new Bag<Component>()).size());
        assertTrue(entity.getComponents(new Bag<Component>()).get(0) instanceof TestComponent);
        TestComponent test = (TestComponent) entity.getComponents(new Bag<Component>()).get(0);
        assertEquals("b", test.getA());
    }

    @Test
    public void testMultiple() {
        Entity entity = ArtemisTemplateEntityLoader.loadTemplateToWorld(world, "entity/multiple.json", new ClasspathFileHandleResolver());
        assertEquals(2, entity.getComponents(new Bag<Component>()).size());
        for (Component component : entity.getComponents(new Bag<Component>())) {
            if (component instanceof TestComponent) {
                assertEquals("b", ((TestComponent) component).getA());
            } else {
                assertEquals("c", ((Test2Component) component).getB());
            }
        }
    }

    @Test
    public void testExtends() {
        Entity entity = ArtemisTemplateEntityLoader.loadTemplateToWorld(world, "entity/extend.json", new ClasspathFileHandleResolver());
        assertEquals(2, entity.getComponents(new Bag<Component>()).size());
        for (Component component : entity.getComponents(new Bag<Component>())) {
            if (component instanceof TestComponent) {
                assertEquals("b", ((TestComponent) component).getA());
            } else {
                assertEquals("c", ((Test2Component) component).getB());
            }
        }
    }
}
