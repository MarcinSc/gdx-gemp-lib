package com.gempukku.libgdx.lib.artemis.camera.orthographic;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;

public class OrthographicCameraControl implements CameraControl {
    private CameraSystem cameraSystem;

    private String cameraName;

    public OrthographicCameraControl(CameraSystem cameraSystem) {
        this.cameraSystem = cameraSystem;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    @Override
    public Vector2 getCameraPosition() {
        return ((OrthographicCameraController) cameraSystem.getCameraController(cameraName)).getCameraPosition(cameraName);
    }

    @Override
    public Vector2 getCameraViewport() {
        return ((OrthographicCameraController) cameraSystem.getCameraController(cameraName)).getCameraViewport(cameraName);
    }

    @Override
    public void moveBy(float x, float y) {
        ((OrthographicCameraController) cameraSystem.getCameraController(cameraName)).moveBy(cameraName, x, y);
    }

    @Override
    public void moveTo(float x, float y) {
        ((OrthographicCameraController) cameraSystem.getCameraController(cameraName)).moveTo(cameraName, x, y);
    }

    @Override
    public void resizeTo(float width, float height) {
        ((OrthographicCameraController) cameraSystem.getCameraController(cameraName)).resizeTo(cameraName, width, height);
    }
}
