package com.gempukku.libgdx.lib.camera2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.lib.camera2d.constraint.CameraConstraint;
import com.gempukku.libgdx.lib.camera2d.focus.CameraFocus;

public class FocusCameraController {
    private Camera camera;
    private CameraFocus cameraFocus;
    private Array<CameraConstraint> constraints = new Array<>();

    private Vector2 tmpVector = new Vector2();

    public FocusCameraController(Camera camera, CameraFocus cameraFocus,
                                 CameraConstraint... constraints) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.constraints.addAll(constraints);
    }

    public void setCameraFocus(CameraFocus cameraFocus) {
        this.cameraFocus = cameraFocus;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void addCameraConstraint(CameraConstraint cameraConstraint) {
        constraints.add(cameraConstraint);
    }

    public void removeCameraConstraint(CameraConstraint cameraConstraint) {
        constraints.removeValue(cameraConstraint, true);
    }

    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        for (CameraConstraint constraint : constraints) {
            constraint.applyConstraint(camera, focus, delta);
        }
    }
}
