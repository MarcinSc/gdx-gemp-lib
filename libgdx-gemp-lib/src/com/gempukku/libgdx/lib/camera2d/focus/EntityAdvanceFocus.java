package com.gempukku.libgdx.lib.camera2d.focus;

import com.badlogic.gdx.math.Vector2;

public class EntityAdvanceFocus implements WeightedCameraFocus {
    private FacingPositionProvider entity;
    private float advanceDistance;
    private float weight;

    public EntityAdvanceFocus(FacingPositionProvider entity, float advanceDistance) {
        this(entity, advanceDistance, 1f);
    }

    public EntityAdvanceFocus(FacingPositionProvider entity, float advanceDistance, float weight) {
        this.entity = entity;
        this.advanceDistance = advanceDistance;
        this.weight = weight;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        Vector2 position = entity.getPosition(focus);
        float x = position.x;
        float y = position.y;

        Vector2 facingMultiplier = entity.getFacingMultiplier(focus);
        return facingMultiplier.set(facingMultiplier.x * advanceDistance + x, facingMultiplier.y * advanceDistance + y);
    }
}
