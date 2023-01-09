package com.gempukku.libgdx.camera2d;

public interface CameraControl {
    void moveBy(float x, float y);

    void moveTo(float x, float y);

    void resizeTo(float width, float height);
}
