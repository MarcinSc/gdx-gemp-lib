package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import com.gempukku.libgdx.camera2d.UpdateCameraControl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LockedToWindowCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noChanges() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new UpdateCameraControl(camera), new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 0);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustRight() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new UpdateCameraControl(camera), new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        cameraConstraint.applyConstraint(camera, new Vector2(-2, 0f), 0);

        assertEquals(-1, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustLeft() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new UpdateCameraControl(camera), new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        cameraConstraint.applyConstraint(camera, new Vector2(2, 0f), 0);

        assertEquals(1, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustUp() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new UpdateCameraControl(camera), new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        cameraConstraint.applyConstraint(camera, new Vector2(0, -2f), 0);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(-1.25f, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustDown() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);

        LockedToWindowCamera2DConstraint cameraConstraint = new LockedToWindowCamera2DConstraint(new UpdateCameraControl(camera), new Rectangle(0.25f, 0.25f, 0.5f, 0.5f));

        cameraConstraint.applyConstraint(camera, new Vector2(0, 2f), 0);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(1.25f, camera.position.y, TEST_DELTA);
    }
}
