package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.gempukku.libgdx.lib.LibGDXTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SceneCameraConstraintTest extends LibGDXTest {
    @Test
    public void noAdjustment() {
        SceneCameraConstraint cameraConstraint = new SceneCameraConstraint(new Rectangle(0, 0, 200, 100));

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(10, 10f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(10, camera.position.x, 0.0001f);
        assertEquals(10, camera.position.y, 0.0001f);
    }

    @Test
    public void adjustRight() {
        SceneCameraConstraint cameraConstraint = new SceneCameraConstraint(new Rectangle(0, 0, 200, 100));

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(0, 10f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(2, camera.position.x, 0.0001f);
        assertEquals(10, camera.position.y, 0.0001f);
    }

    @Test
    public void adjustLeft() {
        SceneCameraConstraint cameraConstraint = new SceneCameraConstraint(new Rectangle(0, 0, 200, 100));

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(200, 10f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(198, camera.position.x, 0.0001f);
        assertEquals(10, camera.position.y, 0.0001f);
    }

    @Test
    public void adjustUp() {
        SceneCameraConstraint cameraConstraint = new SceneCameraConstraint(new Rectangle(0, 0, 200, 100));

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(10, 0f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(10, camera.position.x, 0.0001f);
        assertEquals(1.5f, camera.position.y, 0.0001f);
    }

    @Test
    public void adjustDown() {
        SceneCameraConstraint cameraConstraint = new SceneCameraConstraint(new Rectangle(0, 0, 200, 100));

        OrthographicCamera camera = new OrthographicCamera(4, 3);
        camera.position.set(10, 100f, 0);
        camera.update();

        cameraConstraint.applyConstraint(camera, 0);

        assertEquals(10, camera.position.x, 0.0001f);
        assertEquals(98.5f, camera.position.y, 0.0001f);
    }
}
