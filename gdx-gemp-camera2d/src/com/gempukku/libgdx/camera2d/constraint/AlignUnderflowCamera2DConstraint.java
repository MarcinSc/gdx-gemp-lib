package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

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
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float previousX = camera.position.x;
        float previousY = camera.position.y;

        if (camera.viewportWidth > bounds.width)
            camera.position.x = bounds.x + applyX(camera.viewportWidth, bounds.width) + camera.viewportWidth / 2;
        if (camera.viewportHeight > bounds.height)
            camera.position.y = bounds.y + applyY(camera.viewportHeight, bounds.height) + camera.viewportHeight / 2;

        if (camera.position.x != previousX || camera.position.y != previousY)
            camera.update();
    }

    private float applyX(float width, float availableWidth) {
        return Align.isLeft(alignment) ? 0 : (Align.isRight(alignment) ? (availableWidth - width) : ((availableWidth - width) / 2));
    }

    private float applyY(float height, float availableHeight) {
        return Align.isTop(alignment) ? 0 : (Align.isBottom(alignment) ? (availableHeight - height) : ((availableHeight - height) / 2));
    }
}
