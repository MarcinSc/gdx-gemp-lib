package com.gempukku.libgdx.lib.artemis.camera.orthographic;

import com.badlogic.gdx.graphics.Camera;
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
    public Camera getCamera() {
        return cameraSystem.getCamera(cameraName);
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
