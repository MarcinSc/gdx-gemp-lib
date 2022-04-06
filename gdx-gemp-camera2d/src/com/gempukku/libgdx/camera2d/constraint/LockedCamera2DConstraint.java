package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class LockedCamera2DConstraint implements Camera2DConstraint {
    private Vector2 anchor = new Vector2();

    public LockedCamera2DConstraint(Vector2 anchor) {
        setAnchor(anchor);
    }

    public void setAnchor(Vector2 anchor) {
        this.anchor.set(anchor);
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float currentAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float currentAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;
        float moveX = camera.viewportWidth * (currentAnchorX - anchor.x);
        float moveY = camera.viewportHeight * (currentAnchorY - anchor.y);
        camera.position.x += moveX;
        camera.position.y += moveY;
        if (moveX != 0 || moveY != 0)
            camera.update();
    }
}
