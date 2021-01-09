package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FitAllCameraFocusTest extends LibGDXTest {
    @Test
    public void oneFocus() {
        FitAllCameraFocus focus = new FitAllCameraFocus();
        focus.addCameraFocus(new CameraFocusImpl(new Vector2(0, 0)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(0, result.x, 0.0001f);
        assertEquals(0, result.y, 0.0001f);
    }

    @Test
    public void twoFoci() {
        FitAllCameraFocus focus = new FitAllCameraFocus();
        focus.addCameraFocus(new CameraFocusImpl(new Vector2(0, 0)));
        focus.addCameraFocus(new CameraFocusImpl(new Vector2(2, 2)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
    }

    @Test
    public void threeFoci() {
        FitAllCameraFocus focus = new FitAllCameraFocus();
        focus.addCameraFocus(new CameraFocusImpl(new Vector2(0, 0)));
        focus.addCameraFocus(new CameraFocusImpl(new Vector2(2, 2)));
        focus.addCameraFocus(new CameraFocusImpl(new Vector2(1, 2)));

        Vector2 result = focus.getFocus(new Vector2());

        assertEquals(1, result.x, 0.0001f);
        assertEquals(1, result.y, 0.0001f);
    }

    private static class CameraFocusImpl implements CameraFocus {
        private Vector2 position;

        public CameraFocusImpl(Vector2 position) {
            this.position = position;
        }

        @Override
        public Vector2 getFocus(Vector2 focus) {
            return focus.set(position);
        }
    }
}
