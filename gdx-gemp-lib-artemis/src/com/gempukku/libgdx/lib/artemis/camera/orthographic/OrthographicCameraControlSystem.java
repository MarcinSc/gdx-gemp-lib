package com.gempukku.libgdx.lib.artemis.camera.orthographic;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.camera2d.Camera2DController;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;

public class OrthographicCameraControlSystem extends BaseSystem {
    private CameraSystem cameraSystem;

    private OrthographicCameraControl orthographicCameraControl;

    private ObjectMap<String, Camera2DController> cameraControllers = new ObjectMap<>();

    @Override
    protected void initialize() {
        orthographicCameraControl = new OrthographicCameraControl(cameraSystem);
    }

    public void setCameraController(String cameraName, Camera2DController camera2DController) {
        cameraControllers.put(cameraName, camera2DController);
    }

    @Override
    protected void processSystem() {
        float delta = world.getDelta();
        for (ObjectMap.Entry<String, Camera2DController> camera2DController : cameraControllers) {
            orthographicCameraControl.setCameraName(camera2DController.key);
            camera2DController.value.update(delta, orthographicCameraControl);
        }
    }
}
