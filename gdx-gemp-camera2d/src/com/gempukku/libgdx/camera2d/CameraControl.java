package com.gempukku.libgdx.camera2d;

import com.badlogic.gdx.math.Vector2;

public interface CameraControl {
    Vector2 getCameraPosition();

    Vector2 getCameraViewport();

    void moveBy(float x, float y);

    void moveTo(float x, float y);

    void resizeTo(float width, float height);
}
