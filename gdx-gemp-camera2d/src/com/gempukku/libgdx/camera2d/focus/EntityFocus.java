package com.gempukku.libgdx.camera2d.focus;

import com.badlogic.gdx.math.Vector2;

public class EntityFocus implements WeightedCamera2DFocus {
    private PositionProvider entity;
    private float weight;

    public EntityFocus(PositionProvider entity) {
        this(entity, 1f);
    }

    public EntityFocus(PositionProvider entity, float weight) {
        this.entity = entity;
        this.weight = weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        return entity.getPosition(focus);
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
