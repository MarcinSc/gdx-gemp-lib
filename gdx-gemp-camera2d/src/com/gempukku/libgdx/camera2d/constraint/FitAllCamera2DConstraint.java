package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.camera2d.focus.Camera2DFocus;

public class FitAllCamera2DConstraint implements Camera2DConstraint {
    private Rectangle window = new Rectangle();
    private ObjectSet<Camera2DFocus> cameraFocusArray = new ObjectSet<>();

    private Rectangle tmpRectangle = new Rectangle();
    private Vector2 tmpVector = new Vector2();

    public FitAllCamera2DConstraint(Rectangle window, Camera2DFocus... cameraFoci) {
        setWindow(window);
        cameraFocusArray.addAll(cameraFoci);
    }

    public void setWindow(Rectangle window) {
        this.window.set(window);
    }

    public void addCameraFocus(Camera2DFocus cameraFocus) {
        cameraFocusArray.add(cameraFocus);
    }

    public void removeCameraFocus(Camera2DFocus cameraFocus) {
        cameraFocusArray.remove(cameraFocus);
    }

    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        boolean first = true;
        for (Camera2DFocus cameraFocus : cameraFocusArray) {
            tmpVector = cameraFocus.getFocus(tmpVector);
            if (first) {
                tmpRectangle.set(tmpVector.x, tmpVector.y, 0, 0);
                first = false;
            } else {
                tmpRectangle.merge(tmpVector);
            }
        }
        tmpRectangle.width = Math.max(0.0001f, tmpRectangle.width);
        tmpRectangle.height = Math.max(0.0001f, tmpRectangle.height);

        float cameraAspectRatio = camera.viewportWidth / camera.viewportHeight;

        float desiredWidth = window.width * camera.viewportWidth;
        float desiredHeight = window.height * camera.viewportHeight;

        float desiredAspectRatio = desiredWidth / desiredHeight;
        float requiredAspectRatio = tmpRectangle.width / tmpRectangle.height;

        float windowWidth;
        float windowHeight;
        if (requiredAspectRatio > desiredAspectRatio) {
            windowWidth = tmpRectangle.width / window.width;
            windowHeight = windowWidth / cameraAspectRatio;
        } else {
            windowHeight = tmpRectangle.height / window.height;
            windowWidth = windowHeight * cameraAspectRatio;
        }

        camera.viewportWidth = windowWidth;
        camera.viewportHeight = windowHeight;
        camera.update();
    }
}
