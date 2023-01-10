package com.gempukku.libgdx.camera2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class UpdateCameraControl implements CameraControl {
    private Vector2 tmpCameraPosition = new Vector2();
    private Vector2 tmpCameraViewport = new Vector2();

    private Camera camera;

    public UpdateCameraControl(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Vector2 getCameraPosition() {
        return tmpCameraPosition.set(camera.position.x, camera.position.y);
    }

    @Override
    public Vector2 getCameraViewport() {
        return tmpCameraViewport.set(camera.viewportWidth, camera.viewportHeight);
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
