package com.gempukku.libgdx.lib.artemis.transform;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Matrix4;

public class TransformComponent extends PooledComponent {
    private Matrix4 globalTransform = new Matrix4();
    private Matrix4 localTransform = new Matrix4();

    public Matrix4 getLocalTransform() {
        return localTransform;
    }

    public Matrix4 getGlobalTransform() {
        return globalTransform;
    }

    @Override
    protected void reset() {
        globalTransform.idt();
        localTransform.idt();
    }
}
