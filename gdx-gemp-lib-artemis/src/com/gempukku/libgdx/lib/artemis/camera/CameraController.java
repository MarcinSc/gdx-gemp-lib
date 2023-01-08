package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;

public interface CameraController {
    void setupWithWorld(World world);

    void update(float deltaTime);

    Camera getCamera(String cameraName);

    Entity getCameraEntity(String cameraName);

    void screenResized(int width, int height);
}
