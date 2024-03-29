package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public class SnapToWindowCamera2DConstraint implements Camera2DConstraint {
    private Rectangle window = new Rectangle();
    private Vector2 speed = new Vector2();

    private Vector2 tmpVector = new Vector2();

    public SnapToWindowCamera2DConstraint(Rectangle window, Vector2 speed) {
        setWindow(window);
        setSpeed(speed);
    }

    public void setWindow(Rectangle window) {
        this.window.set(window);
    }

    public void setSpeed(Vector2 speed) {
        this.speed.set(speed);
    }

    @Override
    public void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta) {
        Vector2 cameraPosition = cameraControl.getCameraPosition();
        Vector2 cameraViewport = cameraControl.getCameraViewport();
        float currentAnchorX = 0.5f + (focus.x - cameraPosition.x) / cameraViewport.x;
        float currentAnchorY = 0.5f + (focus.y - cameraPosition.y) / cameraViewport.y;

        Vector2 snapChange = getRequiredChangeToRectangle(window, tmpVector, currentAnchorX, currentAnchorY);
        snapChange.x = Math.signum(snapChange.x) * Math.min(speed.x * delta, Math.abs(snapChange.x));
        snapChange.y = Math.signum(snapChange.y) * Math.min(speed.y * delta, Math.abs(snapChange.y));

        float moveX = cameraViewport.x * snapChange.x;
        float moveY = cameraViewport.y * snapChange.y;
        cameraControl.moveBy(moveX, moveY);
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
