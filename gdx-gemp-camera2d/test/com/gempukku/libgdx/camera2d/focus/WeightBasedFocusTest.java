package com.gempukku.libgdx.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeightBasedFocusTest extends LibGDXTest {
    @Test
    public void oneFocus() {
        WeightBasedFocus focus = new WeightBasedFocus();
        focus.addFocus(new WeightedCamera2DFocusImpl(1, new Vector2(0, 0)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(0, result.x, TEST_DELTA);
        assertEquals(0, result.y, TEST_DELTA);
    }

    @Test
    public void twoFociSameWeight() {
        WeightBasedFocus focus = new WeightBasedFocus();
        focus.addFocus(new WeightedCamera2DFocusImpl(1, new Vector2(0, 0)));
        focus.addFocus(new WeightedCamera2DFocusImpl(1, new Vector2(2, 2)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, TEST_DELTA);
        assertEquals(1, result.y, TEST_DELTA);
    }

    @Test
    public void twoFociDifferentWeight() {
        WeightBasedFocus focus = new WeightBasedFocus();
        focus.addFocus(new WeightedCamera2DFocusImpl(1, new Vector2(0, 0)));
        focus.addFocus(new WeightedCamera2DFocusImpl(0.5f, new Vector2(3, 3)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, TEST_DELTA);
        assertEquals(1, result.y, TEST_DELTA);
    }

    private static class WeightedCamera2DFocusImpl implements WeightedCamera2DFocus {
        private float weight;
        private Vector2 position;

        public WeightedCamera2DFocusImpl(float weight, Vector2 position) {
            this.weight = weight;
            this.position = position;
        }

        @Override
        public float getWeight() {
            return weight;
        }

        @Override
        public Vector2 getFocus(Vector2 focus) {
            return focus.set(position);
        }
    }
}
