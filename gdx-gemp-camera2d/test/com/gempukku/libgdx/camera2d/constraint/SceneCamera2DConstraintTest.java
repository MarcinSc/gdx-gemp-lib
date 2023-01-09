package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.gempukku.libgdx.camera2d.LibGDXTest;
import com.gempukku.libgdx.camera2d.UpdateCameraControl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SceneCamera2DConstraintTest extends LibGDXTest {
    @Test
    public void noAdjustment() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(10, 10f, 0);
        camera.update();

        SceneCamera2DConstraint cameraConstraint = new SceneCamera2DConstraint(new Rectangle(0, 0, 200, 100));

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(10, camera.position.x, TEST_DELTA);
        assertEquals(10, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustRight() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0, 10f, 0);
        camera.update();

        SceneCamera2DConstraint cameraConstraint = new SceneCamera2DConstraint(new Rectangle(0, 0, 200, 100));

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(2, camera.position.x, TEST_DELTA);
        assertEquals(10, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustLeft() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(200, 10f, 0);
        camera.update();

        SceneCamera2DConstraint cameraConstraint = new SceneCamera2DConstraint(new Rectangle(0, 0, 200, 100));

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(198, camera.position.x, TEST_DELTA);
        assertEquals(10, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustUp() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(10, 0f, 0);
        camera.update();

        SceneCamera2DConstraint cameraConstraint = new SceneCamera2DConstraint(new Rectangle(0, 0, 200, 100));

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(10, camera.position.x, TEST_DELTA);
        assertEquals(1.5f, camera.position.y, TEST_DELTA);
    }

    @Test
    public void adjustDown() {
        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(10, 100f, 0);
        camera.update();

        SceneCamera2DConstraint cameraConstraint = new SceneCamera2DConstraint(new Rectangle(0, 0, 200, 100));

        cameraConstraint.applyConstraint(new UpdateCameraControl(camera), null, 0);

        assertEquals(10, camera.position.x, TEST_DELTA);
        assertEquals(98.5f, camera.position.y, TEST_DELTA);
    }
}
