package com.gempukku.libgdx.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityFocusTest extends LibGDXTest {
    @Test
    public void testWeight() {
        EntityFocus focus = new EntityFocus(
                new PositionProviderImpl(new Vector2(0, 0)), 2);

        assertEquals(2f, focus.getWeight(), 0.0001f);
    }

    @Test
    public void testPosition() {
        EntityFocus focus = new EntityFocus(
                new PositionProviderImpl(new Vector2(10, 0)), 2);

        Vector2 result = focus.getFocus(new Vector2());
        assertEquals(10, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }

    private static class PositionProviderImpl implements PositionProvider {
        private Vector2 position;

        public PositionProviderImpl(Vector2 position) {
            this.position = position;
        }

        @Override
        public Vector2 getPosition(Vector2 position) {
            return position.set(this.position);
        }
    }
}
