package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public class PixelPerfectCamera2DConstraint implements Camera2DConstraint {
    @Override
    public void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta) {
        Vector2 cameraPosition = cameraControl.getCameraPosition();
        Vector2 cameraViewport = cameraControl.getCameraViewport();
        float x = MathUtils.round(cameraPosition.x);
        if (MathUtils.round(cameraViewport.x) % 2 == 1)
            x -= 0.5f;

        float y = MathUtils.round(cameraPosition.y);
        if (MathUtils.round(cameraViewport.y) % 2 == 1)
            y -= 0.5f;

        cameraControl.moveTo(x, y);
        cameraControl.resizeTo(MathUtils.round(cameraViewport.x), MathUtils.round(cameraViewport.y));
    }
}
