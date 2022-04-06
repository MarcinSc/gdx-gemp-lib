package com.gempukku.libgdx.camera2d.focus;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;

public class FitAllFocus implements Camera2DFocus {
    private Rectangle tmpRectangle = new Rectangle();

    public ObjectSet<Camera2DFocus> cameraFocusArray = new ObjectSet<>();

    public FitAllFocus(Camera2DFocus... cameraFoci) {
        cameraFocusArray.addAll(cameraFoci);
    }

    public void addCameraFocus(Camera2DFocus cameraFocus) {
        cameraFocusArray.add(cameraFocus);
    }

    public void removeCameraFocus(Camera2DFocus cameraFocus) {
        cameraFocusArray.remove(cameraFocus);
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        boolean first = true;
        for (Camera2DFocus cameraFocus : cameraFocusArray) {
            focus = cameraFocus.getFocus(focus);
            if (first) {
                tmpRectangle.set(focus.x, focus.y, 0, 0);
                first = false;
            } else {
                tmpRectangle.merge(focus);
            }
        }
        return tmpRectangle.getCenter(focus);
    }
}
