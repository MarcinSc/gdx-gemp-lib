package com.gempukku.libgdx.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FitAllFocusTest extends LibGDXTest {
    @Test
    public void oneFocus() {
        FitAllFocus focus = new FitAllFocus();
        focus.addCameraFocus(new Camera2DFocusImpl(new Vector2(0, 0)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(0, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }

    @Test
    public void twoFoci() {
        FitAllFocus focus = new FitAllFocus();
        focus.addCameraFocus(new Camera2DFocusImpl(new Vector2(0, 0)));
        focus.addCameraFocus(new Camera2DFocusImpl(new Vector2(2, 2)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
    }

    @Test
    public void threeFoci() {
        FitAllFocus focus = new FitAllFocus();
        focus.addCameraFocus(new Camera2DFocusImpl(new Vector2(0, 0)));
        focus.addCameraFocus(new Camera2DFocusImpl(new Vector2(2, 2)));
        focus.addCameraFocus(new Camera2DFocusImpl(new Vector2(1, 2)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
    }

    private static class Camera2DFocusImpl implements Camera2DFocus {
        private Vector2 position;

        public Camera2DFocusImpl(Vector2 position) {
            this.position = position;
        }

        @Override
        public Vector2 getFocus(Vector2 focus) {
            return focus.set(position);
        }
    }
}
