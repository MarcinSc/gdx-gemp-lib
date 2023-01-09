package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.camera2d.CameraControl;

public interface Camera2DConstraint {
    void applyConstraint(CameraControl cameraControl, Vector2 focus, float delta);
}
