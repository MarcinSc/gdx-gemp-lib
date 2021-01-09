package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumViewportCameraConstraintTest extends LibGDXTest {
    @Test
    public void noChange() {
        MinimumViewportCameraConstraint cameraConstraint = new MinimumViewportCameraConstraint(1, 1);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(4, camera.viewportWidth, 0.0001f);
        assertEquals(3, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void tooSmallHorizontally() {
        MinimumViewportCameraConstraint cameraConstraint = new MinimumViewportCameraConstraint(8, 1);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void tooSmallVertically() {
        MinimumViewportCameraConstraint cameraConstraint = new MinimumViewportCameraConstraint(1, 6);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void tooSmallBothSides() {
        MinimumViewportCameraConstraint cameraConstraint = new MinimumViewportCameraConstraint(8, 5);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }
}
