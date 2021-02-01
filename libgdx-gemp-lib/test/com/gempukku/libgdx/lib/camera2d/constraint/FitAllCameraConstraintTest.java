package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import com.gempukku.libgdx.lib.camera2d.FocusCameraController;
import com.gempukku.libgdx.lib.camera2d.focus.CameraFocus;
import com.gempukku.libgdx.lib.camera2d.focus.FitAllCameraFocus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FitAllCameraConstraintTest extends LibGDXTest {
    @Test
    public void testWithOneFocus() {
        FitAllCameraConstraint cameraConstraint = new FitAllCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 0));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(0.0002, camera.viewportWidth, 0.0001f);
        assertEquals(0.0002, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void testWithTwoFociHorizontally() {
        FitAllCameraConstraint cameraConstraint = new FitAllCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(1, 0));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(2f, camera.viewportWidth, 0.0001f);
        assertEquals(1.5f, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void testWithTwoFociVertically() {
        FitAllCameraConstraint cameraConstraint = new FitAllCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 1));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(2.6666f, camera.viewportWidth, 0.0001f);
        assertEquals(2f, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void testWithTwoFociBothSides() {
        FitAllCameraConstraint cameraConstraint = new FitAllCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(4, 2));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void testOverlapping() {
        FitAllCameraConstraint cameraConstraint = new FitAllCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCameraFocus(0, 0));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(0.0002, camera.viewportWidth, 0.0001f);
        assertEquals(0.0002, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void testOverlappingFull() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        FocusCameraController cameraController = new FocusCameraController(camera,
                new FitAllCameraFocus(
                        new PositionCameraFocus(0, 0),
                        new PositionCameraFocus(0, 0)
                ),
                new LockedToCameraConstraint(new Vector2(0.5f, 0.5f)),
                new FitAllCameraConstraint(
                        new Rectangle(0.2f, 0.2f, 0.6f, 0.6f),
                        new PositionCameraFocus(0, 0),
                        new PositionCameraFocus(0, 0)
                ),
                new MinimumViewportCameraConstraint(50, 37.5f));

        cameraController.update(0);
        assertEquals(50, camera.viewportWidth, 0.0001f);
        assertEquals(37.5, camera.viewportHeight, 0.0001f);
    }

    private static class PositionCameraFocus implements CameraFocus {
        private float x;
        private float y;

        public PositionCameraFocus(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Vector2 getFocus(Vector2 focus) {
            return focus.set(x, y);
        }
    }
}
