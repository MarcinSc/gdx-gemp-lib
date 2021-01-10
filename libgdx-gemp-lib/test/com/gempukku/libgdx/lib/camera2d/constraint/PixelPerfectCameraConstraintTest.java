package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PixelPerfectCameraConstraintTest extends LibGDXTest {
    @Test
    public void roundX() {
        PixelPerfectCameraConstraint cameraConstraint = new PixelPerfectCameraConstraint();

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0.5f, 0f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(1, camera.position.x, 0.0001);
        assertEquals(0, camera.position.y, 0.0001);
        assertEquals(0, camera.position.z, 0.0001);
    }

    @Test
    public void roundY() {
        PixelPerfectCameraConstraint cameraConstraint = new PixelPerfectCameraConstraint();

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0, 0.5f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(0, camera.position.x, 0.0001);
        assertEquals(1, camera.position.y, 0.0001);
        assertEquals(0, camera.position.z, 0.0001);
    }

    @Test
    public void roundBoth() {
        PixelPerfectCameraConstraint cameraConstraint = new PixelPerfectCameraConstraint();

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0.5f, 0.5f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(1, camera.position.x, 0.0001);
        assertEquals(1, camera.position.y, 0.0001);
        assertEquals(0, camera.position.z, 0.0001);
    }

    @Test
    public void roundWidth() {
        PixelPerfectCameraConstraint cameraConstraint = new PixelPerfectCameraConstraint();

        OrthographicCamera camera = new OrthographicCamera(4.25f, 3);
        camera.position.set(0f, 0f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(4, camera.viewportWidth, 0.0001);
        assertEquals(3, camera.viewportHeight, 0.0001);
    }

    @Test
    public void roundHeight() {
        PixelPerfectCameraConstraint cameraConstraint = new PixelPerfectCameraConstraint();

        OrthographicCamera camera = new OrthographicCamera(4f, 3.25f);
        camera.position.set(0f, 0f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(4, camera.viewportWidth, 0.0001);
        assertEquals(3, camera.viewportHeight, 0.0001);
    }
}
