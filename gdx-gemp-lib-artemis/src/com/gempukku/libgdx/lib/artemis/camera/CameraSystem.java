package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.lib.artemis.event.EventListener;

public class CameraSystem extends BaseSystem {
    private final CameraController cameraController;

    public CameraSystem(CameraController cameraController) {
        this.cameraController = cameraController;
    }

    @Override
    protected void initialize() {
        cameraController.setupWithWorld(world);
    }

    @EventListener
    public void screenResized(ScreenResized screenResized, Entity gameEntity) {
        cameraController.screenResized(screenResized.getWidth(), screenResized.getHeight());
    }

    @Override
    protected void processSystem() {
        cameraController.update(world.getDelta());
    }

    public Camera getCamera() {
        return cameraController.getCamera();
    }

    public Entity getCameraEntity() {
        return cameraController.getCameraEntity();
    }

    public CameraController getCameraController() {
        return cameraController;
    }
}
