package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public class LockedCamera2DConstraint implements Camera2DConstraint {
    private Vector2 anchor = new Vector2();

    public LockedCamera2DConstraint(Vector2 anchor) {
        setAnchor(anchor);
    }

    public void setAnchor(Vector2 anchor) {
        this.anchor.set(anchor);
    }

    @Override
    public void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta) {
        Vector2 cameraPosition = cameraControl.getCameraPosition();
        Vector2 cameraViewport = cameraControl.getCameraViewport();
        float currentAnchorX = 0.5f + (focus.x - cameraPosition.x) / cameraViewport.x;
        float currentAnchorY = 0.5f + (focus.y - cameraPosition.y) / cameraViewport.y;
        float moveX = cameraViewport.x * (currentAnchorX - anchor.x);
        float moveY = cameraViewport.y * (currentAnchorY - anchor.y);
        cameraControl.moveBy(moveX, moveY);
    }
}
