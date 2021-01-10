package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LerpToWindowCameraConstraintTest extends LibGDXTest {
    @Test
    public void noMovement() {
        LerpToWindowCameraConstraint cameraConstraint = new LerpToWindowCameraConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(1, 1), new Vector2(1, 1));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 1f);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void lerpMaximumDistance() {
        LerpToWindowCameraConstraint cameraConstraint = new LerpToWindowCameraConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.5f, 0.5f), new Vector2(0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(3, 0f), 1f);

        assertEquals(1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void lerpMaximumSpeed() {
        LerpToWindowCameraConstraint cameraConstraint = new LerpToWindowCameraConstraint(
                new Rectangle(0.25f, 0.25f, 0.5f, 0.5f), new Vector2(0.5f, 0.5f), new Vector2(0.1f, 0.1f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(3, 0f), 1f);

        assertEquals(0.4f, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }
}
