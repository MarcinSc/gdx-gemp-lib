package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import com.gempukku.libgdx.camera2d.UpdateCameraControl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumViewportCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noChange() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(1, 1);

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(4, camera.viewportWidth, TEST_DELTA);
        assertEquals(3, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void tooSmallHorizontally() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(8, 1);

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(8, camera.viewportWidth, TEST_DELTA);
        assertEquals(6, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void tooSmallVertically() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(1, 6);

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(8, camera.viewportWidth, TEST_DELTA);
        assertEquals(6, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void tooSmallBothSides() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(8, 5);

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(8, camera.viewportWidth, TEST_DELTA);
        assertEquals(6, camera.viewportHeight, TEST_DELTA);
    }
}
