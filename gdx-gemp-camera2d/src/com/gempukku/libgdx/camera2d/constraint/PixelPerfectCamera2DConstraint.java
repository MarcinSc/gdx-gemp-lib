package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public class PixelPerfectCamera2DConstraint implements Camera2DConstraint {
    private CameraControl cameraControl;

    public PixelPerfectCamera2DConstraint(CameraControl cameraControl) {
        this.cameraControl = cameraControl;
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float x = MathUtils.round(camera.position.x);
        if (MathUtils.round(camera.viewportWidth) % 2 == 1)
            x -= 0.5f;

        float y = MathUtils.round(camera.position.y);
        if (MathUtils.round(camera.viewportHeight) % 2 == 1)
            y -= 0.5f;

        cameraControl.moveTo(x, y);
        cameraControl.resizeTo(MathUtils.round(camera.viewportWidth), MathUtils.round(camera.viewportHeight));
    }
}
