package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnapToWindowCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noMovement() {
        SnapToWindowCamera2DConstraint cameraConstraint = new SnapToWindowCamera2DConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 1f);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void snapMaximumDistance() {
        SnapToWindowCamera2DConstraint cameraConstraint = new SnapToWindowCamera2DConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(3, 0f), 1f);

        assertEquals(0.4f, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void snapBelowMaximum() {
        SnapToWindowCamera2DConstraint cameraConstraint = new SnapToWindowCamera2DConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(1f, 1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(3f, 0f), 1f);

        assertEquals(2f, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }
}
