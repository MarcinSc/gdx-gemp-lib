package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumViewportCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noChange() {
        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(1, 1);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(4, camera.viewportWidth, 0.0001f);
        assertEquals(3, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void tooSmallHorizontally() {
        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(8, 1);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void tooSmallVertically() {
        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(1, 6);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }

    @Test
    public void tooSmallBothSides() {
        MinimumViewportCamera2DConstraint cameraConstraint = new MinimumViewportCamera2DConstraint(8, 5);

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(8, camera.viewportWidth, 0.0001f);
        assertEquals(6, camera.viewportHeight, 0.0001f);
    }
}
