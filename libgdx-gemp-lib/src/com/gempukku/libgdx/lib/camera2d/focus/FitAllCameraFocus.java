package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;

public class FitAllCameraFocus implements CameraFocus {
    private Rectangle tmpRectangle = new Rectangle();

    public ObjectSet<CameraFocus> cameraFocusArray = new ObjectSet<>();

    public FitAllCameraFocus(CameraFocus... cameraFoci) {
        cameraFocusArray.addAll(cameraFoci);
    }

    public void addCameraFocus(CameraFocus cameraFocus) {
        cameraFocusArray.add(cameraFocus);
    }

    public void removeCameraFocus(CameraFocus cameraFocus) {
        cameraFocusArray.remove(cameraFocus);
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        boolean first = true;
        for (CameraFocus cameraFocus : cameraFocusArray) {
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
