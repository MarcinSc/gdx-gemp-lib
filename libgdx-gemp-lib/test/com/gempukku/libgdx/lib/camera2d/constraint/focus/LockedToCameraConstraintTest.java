package com.gempukku.libgdx.lib.camera2d.constraint.focus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LockedToCameraConstraintTest extends LibGDXTest {
    @Test
    public void noAdjustment() {
        LockedToCameraConstraint cameraConstraint = new LockedToCameraConstraint(new Vector2(0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(0, 0f), 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustPosition() {
        LockedToCameraConstraint cameraConstraint = new LockedToCameraConstraint(new Vector2(0.5f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(1, 0f), 0);

        assertEquals(1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }

    @Test
    public void adjustDifferentAnchor() {
        LockedToCameraConstraint cameraConstraint = new LockedToCameraConstraint(new Vector2(0f, 0.5f));

        OrthographicCamera camera = new OrthographicCamera(4, 3);

        cameraConstraint.applyConstraint(camera, new Vector2(1, 0f), 0);

        assertEquals(3, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
    }
}
