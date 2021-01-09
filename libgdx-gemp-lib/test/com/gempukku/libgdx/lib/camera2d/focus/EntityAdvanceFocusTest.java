package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityAdvanceFocusTest extends LibGDXTest {
    @Test
    public void testWeight() {
        EntityAdvanceFocus focus = new EntityAdvanceFocus(
                new FacingPositionProviderImpl(new Vector2(0, 0), new Vector2(1, 0)), 10, 2);

        assertEquals(2f, focus.getWeight(), 0.0001f);
    }

    @Test
    public void faceRight() {
        EntityAdvanceFocus focus = new EntityAdvanceFocus(
                new FacingPositionProviderImpl(new Vector2(0, 0), new Vector2(1, 0)), 10);

        Vector2 result = focus.getFocus(new Vector2());
        assertEquals(10, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }

    @Test
    public void faceUp() {
        EntityAdvanceFocus focus = new EntityAdvanceFocus(
                new FacingPositionProviderImpl(new Vector2(0, 0), new Vector2(0, 1)), 10);

        Vector2 result = focus.getFocus(new Vector2());
        assertEquals(0, result.x, 0.0001f);
        assertEquals(10, result.y, 0.0001f);
    }

    private static class FacingPositionProviderImpl implements FacingPositionProvider {
        private Vector2 position;
        private Vector2 facing;

        public FacingPositionProviderImpl(Vector2 position, Vector2 facing) {
            this.position = position;
            this.facing = facing;
        }

        @Override
        public Vector2 getFacingMultiplier(Vector2 facingMultiplier) {
            return facingMultiplier.set(facing);
        }

        @Override
        public Vector2 getPosition(Vector2 position) {
            return position.set(this.position);
        }
    }
}
