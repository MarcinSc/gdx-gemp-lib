package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import com.gempukku.libgdx.camera2d.UpdateCameraControl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnapToWindowCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noMovement() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        SnapToWindowCamera2DConstraint cameraConstraint = new SnapToWindowCamera2DConstraint(
                new UpdateCameraControl(camera),
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 1f);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void snapMaximumDistance() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        SnapToWindowCamera2DConstraint cameraConstraint = new SnapToWindowCamera2DConstraint(
                new UpdateCameraControl(camera),
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        cameraConstraint.applyConstraint(camera, new Vector2(3, 0f), 1f);

        assertEquals(0.4f, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void snapBelowMaximum() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        SnapToWindowCamera2DConstraint cameraConstraint = new SnapToWindowCamera2DConstraint(
                new UpdateCameraControl(camera),
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(1f, 1f));

        cameraConstraint.applyConstraint(camera, new Vector2(3f, 0f), 1f);

        assertEquals(2f, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }
}
