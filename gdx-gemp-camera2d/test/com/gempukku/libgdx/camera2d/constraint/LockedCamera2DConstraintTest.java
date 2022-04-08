package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LockedCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noAdjustment() {
        LockedCamera2DConstraint cameraConstraint = new LockedCamera2DConstraint(new Vector2(0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 0);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustPosition() {
        LockedCamera2DConstraint cameraConstraint = new LockedCamera2DConstraint(new Vector2(0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(1, 0f), 0);

        assertEquals(1, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustDifferentAnchor() {
        LockedCamera2DConstraint cameraConstraint = new LockedCamera2DConstraint(new Vector2(0f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(1, 0f), 0);

        assertEquals(3, camera.position.x, TEST_DELTA);
        assertEquals(0, camera.position.y, TEST_DELTA);
    }
}
