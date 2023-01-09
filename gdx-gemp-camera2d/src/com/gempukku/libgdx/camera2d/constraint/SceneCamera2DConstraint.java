package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public class SceneCamera2DConstraint implements Camera2DConstraint {
    private Rectangle bounds = new Rectangle();

    private Vector2 tmpVector1 = new Vector2();
    private Vector2 tmpVector2 = new Vector2();

    public SceneCamera2DConstraint(Rectangle bounds) {
        setBounds(bounds);
    }

    public void setBounds(Rectangle bounds) {
        this.bounds.set(bounds);
    }

    @Override
    public void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta) {
        Camera camera = cameraControl.getCamera();
        Vector2 visibleMin = tmpVector1.set(camera.position.x, camera.position.y).add(-camera.viewportWidth / 2f, -camera.viewportHeight / 2f);
        Vector2 visibleMax = tmpVector2.set(camera.position.x, camera.position.y).add(+camera.viewportWidth / 2f, +camera.viewportHeight / 2f);

        float moveX = Math.min(Math.max(0, bounds.x - visibleMin.x), bounds.x + bounds.width - visibleMax.x);
        float moveY = Math.min(Math.max(0, bounds.y - visibleMin.y), bounds.y + bounds.height - visibleMax.y);

        cameraControl.moveBy(moveX, moveY);
    }
}
