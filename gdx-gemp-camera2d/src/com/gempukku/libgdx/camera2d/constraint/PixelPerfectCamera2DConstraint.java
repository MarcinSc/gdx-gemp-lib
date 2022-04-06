package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PixelPerfectCamera2DConstraint implements Camera2DConstraint {
    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        camera.position.x = MathUtils.round(camera.position.x);
        camera.position.y = MathUtils.round(camera.position.y);
        camera.viewportWidth = MathUtils.round(camera.viewportWidth);
        camera.viewportHeight = MathUtils.round(camera.viewportHeight);

        if (MathUtils.round(camera.viewportWidth) % 2 == 1)
            camera.position.x -= 0.5f;
        if (MathUtils.round(camera.viewportHeight) % 2 == 1)
            camera.position.y -= 0.5f;

        camera.update();
    }
}
