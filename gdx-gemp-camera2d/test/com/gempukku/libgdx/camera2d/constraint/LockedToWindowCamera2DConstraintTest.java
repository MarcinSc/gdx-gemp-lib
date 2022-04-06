package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LockedToWindowCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noChanges() {
        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustRight() {
        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(-2, 0f), 0);

        assertEquals(-1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustLeft() {
        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(2, 0f), 0);

        assertEquals(1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustUp() {
        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, -2f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(-1.25f, camera.position.y, 0.0001);
    }

    @Test
    public void adjustDown() {
        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 2f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(1.25f, camera.position.y, 0.0001);
    }
}
