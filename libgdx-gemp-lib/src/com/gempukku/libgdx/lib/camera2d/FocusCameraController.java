package com.gempukku.libgdx.lib.camera2d;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.lib.camera2d.constraint.CameraConstraint;
import com.gempukku.libgdx.lib.camera2d.constraint.focus.CameraFocusConstraint;
import com.gempukku.libgdx.lib.camera2d.focus.CameraFocus;

public class FocusCameraController {
    private Camera camera;
    private CameraFocus cameraFocus;
    private CameraFocusConstraint[] focusConstraints;
    private CameraConstraint[] constraints;

    private Vector2 tmpVector = new Vector2();

    public FocusCameraController(Camera camera, CameraFocus cameraFocus,
                                 CameraFocusConstraint[] focusConstraints,
                                 CameraConstraint... constraints) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.focusConstraints = focusConstraints;
        this.constraints = constraints;
    }

    public void setCameraFocus(CameraFocus cameraFocus) {
        this.cameraFocus = cameraFocus;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        for (CameraFocusConstraint constraint : focusConstraints) {
            constraint.applyConstraint(camera, focus, delta);
        }
        for (CameraConstraint constraint : constraints) {
            constraint.applyConstraint(camera, delta);
        }
    }
}
