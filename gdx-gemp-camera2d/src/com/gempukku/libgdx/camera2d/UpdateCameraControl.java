package com.gempukku.libgdx.camera2d;

import com.badlogic.gdx.graphics.Camera;

public class UpdateCameraControl implements CameraControl {
    private Camera camera;

    public UpdateCameraControl(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void moveBy(float x, float y) {
        camera.position.x += x;
        camera.position.y += y;
        camera.update();
    }

    @Override
    public void moveTo(float x, float y) {
        camera.position.x = x;
        camera.position.y = y;
        camera.update();
    }

    @Override
    public void resizeTo(float width, float height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}
