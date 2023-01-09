package com.gempukku.libgdx.test.system;

import com.badlogic.ashley.core.EntitySystem;
import com.gempukku.libgdx.camera2d.Camera2DController;
import com.gempukku.libgdx.camera2d.CameraControl;

public class CameraSystem extends EntitySystem {
    private Camera2DController constraintCameraController;
    private CameraControl cameraControl;

    public CameraSystem(int priority) {
        super(priority);
    }

    public void setConstraintCameraController(Camera2DController constraintCameraController) {
        this.constraintCameraController = constraintCameraController;
    }

    public void setCameraControl(CameraControl cameraControl) {
        this.cameraControl = cameraControl;
    }

    @Override
    public void update(float deltaTime) {
        constraintCameraController.update(deltaTime, cameraControl);
    }
}
