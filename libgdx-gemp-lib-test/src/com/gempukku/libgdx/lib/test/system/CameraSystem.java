package com.gempukku.libgdx.lib.test.system;

import com.badlogic.ashley.core.EntitySystem;
import com.gempukku.libgdx.lib.camera2d.FocusCameraController;

public class CameraSystem extends EntitySystem {
    private FocusCameraController constraintCameraController;

    public CameraSystem(int priority) {
        super(priority);
    }

    public void setConstraintCameraController(FocusCameraController constraintCameraController) {
        this.constraintCameraController = constraintCameraController;
    }

    @Override
    public void update(float deltaTime) {
        constraintCameraController.update(deltaTime);
    }
}
