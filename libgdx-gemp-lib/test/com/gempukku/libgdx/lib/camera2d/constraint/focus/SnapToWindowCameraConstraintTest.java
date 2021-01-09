package com.gempukku.libgdx.lib.camera2d.constraint.focus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnapToWindowCameraConstraintTest extends LibGDXTest {
    @Test
    public void noMovement() {
        SnapToWindowCameraConstraint cameraConstraint = new SnapToWindowCameraConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 1f);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void snapMaximumDistance() {
        SnapToWindowCameraConstraint cameraConstraint = new SnapToWindowCameraConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(3, 0f), 1f);

        assertEquals(0.4f, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void snapBelowMaximum() {
        SnapToWindowCameraConstraint cameraConstraint = new SnapToWindowCameraConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(1f, 1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(3f, 0f), 1f);

        assertEquals(2f, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }
}
