package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Vector2;

public interface FacingPositionProvider extends PositionProvider {
    Vector2 getFacingMultiplier(Vector2 facingMultiplier);
}
