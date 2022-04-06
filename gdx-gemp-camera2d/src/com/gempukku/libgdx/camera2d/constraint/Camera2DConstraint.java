package com.gempukku.libgdx.camera2d.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public interface Camera2DConstraint {
    void applyConstraint(Camera camera, Vector2 focus, float delta);
}
