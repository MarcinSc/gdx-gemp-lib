package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LerpToWindowCamera2DConstraint implements Camera2DConstraint {
    private Rectangle window = new Rectangle();
    private Vector2 partPerSecond = new Vector2();
    private Vector2 maxSpeed = new Vector2();

    private Vector2 tmpVector = new Vector2();

    public LerpToWindowCamera2DConstraint(Rectangle window, Vector2 partPerSecond, Vector2 maxSpeed) {
        setWindow(window);
        setPartPerSecond(partPerSecond);
        setMaxSpeed(maxSpeed);
    }

    public void setWindow(Rectangle window) {
        this.window.set(window);
    }

    public void setPartPerSecond(Vector2 partPerSecond) {
        this.partPerSecond.set(partPerSecond);
    }

    public void setMaxSpeed(Vector2 maxSpeed) {
        this.maxSpeed.set(maxSpeed);
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        float currentAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float currentAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;

        Vector2 snapChange = getRequiredChangeToRectangle(window, tmpVector, currentAnchorX, currentAnchorY);
        snapChange.x = Math.signum(snapChange.x) * Math.min(maxSpeed.x * delta, Math.abs(snapChange.x * partPerSecond.x * delta));
        snapChange.y = Math.signum(snapChange.y) * Math.min(maxSpeed.y * delta, Math.abs(snapChange.y * partPerSecond.y * delta));

        float moveX = camera.viewportWidth * snapChange.x;
        float moveY = camera.viewportHeight * snapChange.y;
        camera.position.x += moveX;
        camera.position.y += moveY;
        if (moveX != 0 || moveY != 0)
            camera.update();
    }

    private Vector2 getRequiredChangeToRectangle(Rectangle rectangle, Vector2 tmpVector, float desiredAnchorX, float desiredAnchorY) {
        Vector2 requiredChange = tmpVector.set(0, 0);
        if (desiredAnchorX < rectangle.x) {
            requiredChange.x = desiredAnchorX - rectangle.x;
        } else if (desiredAnchorX > rectangle.x + rectangle.width) {
            requiredChange.x = desiredAnchorX - (rectangle.x + rectangle.width);
        }
        if (desiredAnchorY < rectangle.y) {
            requiredChange.y = desiredAnchorY - rectangle.y;
        } else if (desiredAnchorY > rectangle.y + rectangle.height) {
            requiredChange.y = desiredAnchorY - (rectangle.y + rectangle.height);
        }
        return requiredChange;
    }

}
