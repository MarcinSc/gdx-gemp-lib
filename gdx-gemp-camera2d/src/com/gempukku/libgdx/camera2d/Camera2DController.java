package com.gempukku.libgdx.camera2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.camera2d.constraint.Camera2DConstraint;
import com.gempukku.libgdx.camera2d.focus.Camera2DFocus;

public class Camera2DController {
    private Camera2DFocus cameraFocus;
    private Array<Camera2DConstraint> constraints = new Array<>();

    private Vector2 tmpVector = new Vector2();

    public Camera2DController(Camera2DFocus cameraFocus,
                              Camera2DConstraint... constraints) {
        this.cameraFocus = cameraFocus;
        this.constraints.addAll(constraints);
    }

    public void setCameraFocus(Camera2DFocus cameraFocus) {
        this.cameraFocus = cameraFocus;
    }

    public void addCameraConstraint(Camera2DConstraint cameraConstraint) {
        constraints.add(cameraConstraint);
    }

    public void removeCameraConstraint(Camera2DConstraint cameraConstraint) {
        constraints.removeValue(cameraConstraint, true);
    }

    public void update(float delta, CameraControl cameraControl) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        for (Camera2DConstraint constraint : constraints) {
            constraint.applyConstraint(cameraControl, focus, delta);
        }
    }
}
