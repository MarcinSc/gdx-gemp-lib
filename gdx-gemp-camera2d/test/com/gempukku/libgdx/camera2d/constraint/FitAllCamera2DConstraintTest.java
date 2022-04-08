package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import com.gempukku.libgdx.camera2d.focus.Camera2DFocus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FitAllCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void testWithOneFocus() {
        FitAllCamera2DConstraint cameraConstraint = new FitAllCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(0, 0));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(0, camera.viewportWidth, TEST_DELTA);
        assertEquals(0, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void testWithTwoFociHorizontally() {
        FitAllCamera2DConstraint cameraConstraint = new FitAllCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(1, 0));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(2f, camera.viewportWidth, TEST_DELTA);
        assertEquals(1.5f, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void testWithTwoFociVertically() {
        FitAllCamera2DConstraint cameraConstraint = new FitAllCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(0, 1));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(2.6666f, camera.viewportWidth, TEST_DELTA);
        assertEquals(2f, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void testWithTwoFociBothSides() {
        FitAllCamera2DConstraint cameraConstraint = new FitAllCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(0, 0));
        cameraConstraint.addCameraFocus(
                new PositionCamera2DFocus(4, 2));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(8, camera.viewportWidth, TEST_DELTA);
        assertEquals(6, camera.viewportHeight, TEST_DELTA);
    }

    private static class PositionCamera2DFocus implements Camera2DFocus {
        private float x;
        private float y;

        public PositionCamera2DFocus(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Vector2 getFocus(Vector2 focus) {
            return focus.set(x, y);
        }
    }
}
