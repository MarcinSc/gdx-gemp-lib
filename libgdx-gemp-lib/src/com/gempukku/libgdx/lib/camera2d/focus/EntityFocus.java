package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Vector2;

public class EntityFocus implements WeightedCameraFocus {
    private PositionProvider entity;
    private float weight;
    private float x;
    private float y;

    public EntityFocus(PositionProvider entity) {
        this(entity, 1f);
    }

    public EntityFocus(PositionProvider entity, float weight) {
        this(entity, weight, 0, 0);
    }

    public EntityFocus(PositionProvider entity, float weight, float x, float y) {
        this.entity = entity;
        this.weight = weight;
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        return entity.getPosition(focus).add(x, y);
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
