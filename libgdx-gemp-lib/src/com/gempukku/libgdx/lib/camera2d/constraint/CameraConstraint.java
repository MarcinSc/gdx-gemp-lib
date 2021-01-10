package com.gempukku.libgdx.lib.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public interface CameraConstraint {
    void applyConstraint(Camera camera, Vector2 focus, float delta);
}
