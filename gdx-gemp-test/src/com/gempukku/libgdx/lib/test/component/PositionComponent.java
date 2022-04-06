package com.gempukku.libgdx.lib.test.component;

import com.badlogic.gdx.math.Vector2;

public class PositionComponent extends DirtyComponent {
    private float x;
    private float y;

    public Vector2 getPosition(Vector2 position) {
        return position.set(x, y);
    }

    public void setPosition(float x, float y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            setDirty();
        }
    }
}
