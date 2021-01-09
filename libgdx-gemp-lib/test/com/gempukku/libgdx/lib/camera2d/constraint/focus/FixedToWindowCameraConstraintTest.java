package com.gempukku.libgdx.lib.camera2d.constraint.focus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FixedToWindowCameraConstraintTest extends LibGDXTest {
    @Test
    public void noChanges() {
        FixedToWindowCameraConstraint cameraConstraint = new FixedToWindowCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustRight() {
        FixedToWindowCameraConstraint cameraConstraint = new FixedToWindowCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(-2, 0f), 0);

        assertEquals(-1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustLeft() {
        FixedToWindowCameraConstraint cameraConstraint = new FixedToWindowCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(2, 0f), 0);

        assertEquals(1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustUp() {
        FixedToWindowCameraConstraint cameraConstraint = new FixedToWindowCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, -2f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(-1.25f, camera.position.y, 0.0001);
    }

    @Test
    public void adjustDown() {
        FixedToWindowCameraConstraint cameraConstraint = new FixedToWindowCameraConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 2f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(1.25f, camera.position.y, 0.0001);
    }
}
