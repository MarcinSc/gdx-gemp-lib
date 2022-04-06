package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class MinimumViewportCamera2DConstraint implements Camera2DConstraint {
    private float width;
    private float height;

    public MinimumViewportCamera2DConstraint(float width, float height) {
        setSize(width, height);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float cameraAspectRatio = camera.viewportWidth / camera.viewportHeight;

        float resultWidth = Math.max(width, camera.viewportWidth);
        float resultHeight = Math.max(height, camera.viewportHeight);

        float resultCameraAspectRatio = resultWidth / resultHeight;
        if (resultCameraAspectRatio > cameraAspectRatio) {
            camera.viewportWidth = resultWidth;
            camera.viewportHeight = resultWidth / cameraAspectRatio;
        } else {
            camera.viewportHeight = resultHeight;
            camera.viewportWidth = resultHeight * cameraAspectRatio;
        }
        camera.update();
    }
}
