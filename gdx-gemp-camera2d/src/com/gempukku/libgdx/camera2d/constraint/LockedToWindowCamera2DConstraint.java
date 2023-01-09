package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public class LockedToWindowCamera2DConstraint implements Camera2DConstraint {
    private Rectangle window = new Rectangle();

    private Vector2 tmpVector = new Vector2();

    public LockedToWindowCamera2DConstraint(Rectangle window) {
        setWindow(window);
    }

    public void setWindow(Rectangle window) {
        this.window.set(window);
    }

    @Override
    public void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta) {
        Camera camera = cameraControl.getCamera();
        float currentAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float currentAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;
        Vector2 snapChange = getRequiredChangeToRectangle(window, tmpVector, currentAnchorX, currentAnchorY);
        float moveX = camera.viewportWidth * snapChange.x;
        float moveY = camera.viewportHeight * snapChange.y;
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
