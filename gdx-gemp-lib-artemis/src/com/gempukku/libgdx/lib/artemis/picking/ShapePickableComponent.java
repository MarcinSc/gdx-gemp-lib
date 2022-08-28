package com.gempukku.libgdx.lib.artemis.picking;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class ShapePickableComponent extends PooledComponent {
    private String shape;
    private Matrix4 transform = new Matrix4();
    private String positionDataType;
    private Array<String> pickingMask;

    public String getShape() {
        return shape;
    }

    public Matrix4 getTransform() {
        return transform;
    }

    public String getPositionDataType() {
        return positionDataType;
    }

    public Array<String> getPickingMask() {
        return pickingMask;
    }

    @Override
    protected void reset() {
        shape = null;
        transform.idt();
        positionDataType = null;
        pickingMask = null;
    }
}
