package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;

public interface CameraConstraint {
    void applyConstraint(Camera camera, float delta);
}
