package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import com.gempukku.libgdx.camera2d.UpdateCameraControl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PixelPerfectCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void roundX() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0.5f, 0f, 0);
        camera.update();

        PixelPerfectCamera2DConstraint cameraConstraint = new PixelPerfectCamera2DConstraint(new UpdateCameraControl(camera));

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(1, camera.position.x, TEST_DELTA);
        assertEquals(-0.5f, camera.position.y, TEST_DELTA);
        assertEquals(0f, camera.position.z, TEST_DELTA);
    }

    @Test
    public void roundY() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0, 0.5f, 0);
        camera.update();

        PixelPerfectCamera2DConstraint cameraConstraint = new PixelPerfectCamera2DConstraint(new UpdateCameraControl(camera));

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(0, camera.position.x, TEST_DELTA);
        assertEquals(0.5f, camera.position.y, TEST_DELTA);
        assertEquals(0, camera.position.z, TEST_DELTA);
    }

    @Test
    public void roundBoth() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0.5f, 0.5f, 0);
        camera.update();

        PixelPerfectCamera2DConstraint cameraConstraint = new PixelPerfectCamera2DConstraint(new UpdateCameraControl(camera));

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(1, camera.position.x, TEST_DELTA);
        assertEquals(0.5f, camera.position.y, TEST_DELTA);
        assertEquals(0, camera.position.z, TEST_DELTA);
    }

    @Test
    public void roundWidth() {
        OrthographicCamera camera = new OrthographicCamera(4.25f, 3);
        camera.position.set(0f, 0f, 0);
        camera.update();

        PixelPerfectCamera2DConstraint cameraConstraint = new PixelPerfectCamera2DConstraint(new UpdateCameraControl(camera));

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(4, camera.viewportWidth, TEST_DELTA);
        assertEquals(3, camera.viewportHeight, TEST_DELTA);
    }

    @Test
    public void roundHeight() {
        OrthographicCamera camera = new OrthographicCamera(4f, 3.25f);
        camera.position.set(0f, 0f, 0);
        camera.update();

        PixelPerfectCamera2DConstraint cameraConstraint = new PixelPerfectCamera2DConstraint(new UpdateCameraControl(camera));

        cameraConstraint.applyConstraint(camera, null, 0);

        assertEquals(4, camera.viewportWidth, TEST_DELTA);
        assertEquals(3, camera.viewportHeight, TEST_DELTA);
    }
}
