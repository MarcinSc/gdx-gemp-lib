package com.gempukku.libgdx.lib.test.system;

import com.badlogic.ashley.core.EntitySystem;
import com.gempukku.libgdx.camera2d.Camera2DController;

public class CameraSystem extends EntitySystem {
    private Camera2DController constraintCameraController;

    public CameraSystem(int priority) {
        super(priority);
    }

    public void setConstraintCameraController(Camera2DController constraintCameraController) {
        this.constraintCameraController = constraintCameraController;
    }

    @Override
    public void update(float deltaTime) {
        constraintCameraController.update(deltaTime);
    }
}
