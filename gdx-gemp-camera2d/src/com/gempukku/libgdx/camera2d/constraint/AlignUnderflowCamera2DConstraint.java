package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.camera2d.CameraControl;

public class AlignUnderflowCamera2DConstraint implements Camera2DConstraint {
    private Rectangle bounds;
    private int alignment;

    public AlignUnderflowCamera2DConstraint(Rectangle bounds, int alignment) {
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
    public void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta) {
        Vector2 cameraPosition = cameraControl.getCameraPosition();
        Vector2 cameraViewport = cameraControl.getCameraViewport();
        float resultX = cameraPosition.x;
        float resultY = cameraPosition.y;
        if (cameraViewport.x > bounds.width)
            resultX = bounds.x + applyX(cameraViewport.x, bounds.width) + cameraViewport.x / 2;
        if (cameraViewport.y > bounds.height)
            resultY = bounds.y + applyY(cameraViewport.y, bounds.height) + cameraViewport.y / 2;

        cameraControl.moveTo(resultX, resultY);
    }

    private float applyX(float width, float availableWidth) {
        return Align.isLeft(alignment) ? 0 : (Align.isRight(alignment) ? (availableWidth - width) : ((availableWidth - width) / 2));
    }

    private float applyY(float height, float availableHeight) {
        return Align.isTop(alignment) ? 0 : (Align.isBottom(alignment) ? (availableHeight - height) : ((availableHeight - height) / 2));
    }
}
