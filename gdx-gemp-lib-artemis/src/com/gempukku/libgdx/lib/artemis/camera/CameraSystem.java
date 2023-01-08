package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.lib.artemis.event.EventListener;

public class CameraSystem extends BaseSystem {
    private final CameraController[] cameraControllers;

    public CameraSystem(CameraController... cameraControllers) {
        this.cameraControllers = cameraControllers;
    }

    @Override
    protected void initialize() {
        for (CameraController cameraController : cameraControllers) {
            cameraController.setupWithWorld(world);
        }
    }

    @EventListener
    public void screenResized(ScreenResized screenResized, Entity gameEntity) {
        for (CameraController cameraController : cameraControllers) {
            cameraController.screenResized(screenResized.getWidth(), screenResized.getHeight());
        }
    }

    @Override
    protected void processSystem() {
        for (CameraController cameraController : cameraControllers) {
            cameraController.update(world.getDelta());
        }
    }

    public Camera getCamera(String cameraName) {
        for (CameraController cameraController : cameraControllers) {
            Camera camera = cameraController.getCamera(cameraName);
            if (camera != null)
                return camera;
        }

        return null;
    }

    public Entity getCameraEntity(String cameraName) {
        for (CameraController cameraController : cameraControllers) {
            Entity cameraEntity = cameraController.getCameraEntity(cameraName);
            if (cameraEntity != null)
                return cameraEntity;
        }

        return null;
    }

    public CameraController getCameraController(String cameraName) {
        for (CameraController cameraController : cameraControllers) {
            if (cameraController.getCameraEntity(cameraName) != null)
                return cameraController;
        }

        return null;
    }
}
