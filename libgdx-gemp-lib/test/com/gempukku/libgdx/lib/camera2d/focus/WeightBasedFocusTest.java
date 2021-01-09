package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeightBasedFocusTest extends LibGDXTest {
    @Test
    public void oneFocus() {
        WeightBasedFocus focus = new WeightBasedFocus();
        focus.addFocus(new WeightedCameraFocusImpl(1, new Vector2(0, 0)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(0, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }

    @Test
    public void twoFociSameWeight() {
        WeightBasedFocus focus = new WeightBasedFocus();
        focus.addFocus(new WeightedCameraFocusImpl(1, new Vector2(0, 0)));
        focus.addFocus(new WeightedCameraFocusImpl(1, new Vector2(2, 2)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
    }

    @Test
    public void twoFociDifferentWeight() {
        WeightBasedFocus focus = new WeightBasedFocus();
        focus.addFocus(new WeightedCameraFocusImpl(1, new Vector2(0, 0)));
        focus.addFocus(new WeightedCameraFocusImpl(0.5f, new Vector2(3, 3)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
    }

    private static class WeightedCameraFocusImpl implements WeightedCameraFocus {
        private float weight;
        private Vector2 position;

        public WeightedCameraFocusImpl(float weight, Vector2 position) {
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
