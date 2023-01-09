package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.camera2d.CameraControl;

public class AlignUnderflowCamera2DConstraint implements Camera2DConstraint {
    private CameraControl cameraControl;
    private Rectangle bounds;
    private int alignment;

    public AlignUnderflowCamera2DConstraint(CameraControl cameraControl, Rectangle bounds, int alignment) {
        this.cameraControl = cameraControl;
        this.bounds = bounds;
        this.alignment = alignment;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds.set(bounds);
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float resultX = camera.position.x;
        float resultY = camera.position.y;
        if (camera.viewportWidth > bounds.width)
            resultX = bounds.x + applyX(camera.viewportWidth, bounds.width) + camera.viewportWidth / 2;
        if (camera.viewportHeight > bounds.height)
            resultY = bounds.y + applyY(camera.viewportHeight, bounds.height) + camera.viewportHeight / 2;

        cameraControl.moveTo(resultX, resultY);
    }

    private float applyX(float width, float availableWidth) {
        return Align.isLeft(alignment) ? 0 : (Align.isRight(alignment) ? (availableWidth - width) : ((availableWidth - width) / 2));
    }

    private float applyY(float height, float availableHeight) {
        return Align.isTop(alignment) ? 0 : (Align.isBottom(alignment) ? (availableHeight - height) : ((availableHeight - height) / 2));
    }
}
