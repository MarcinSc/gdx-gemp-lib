package com.gempukku.libgdx.camera2d;

import com.badlogic.gdx.graphics.Camera;

public interface CameraControl {
    Camera getCamera();

    void moveBy(float x, float y);

    void moveTo(float x, float y);

    void resizeTo(float width, float height);
}
