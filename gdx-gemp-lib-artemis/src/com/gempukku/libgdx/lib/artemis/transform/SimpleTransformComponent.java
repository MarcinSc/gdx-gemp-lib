package com.gempukku.libgdx.lib.artemis.transform;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

public class SimpleTransformComponent extends PooledComponent {
    private final Vector3 translate = new Vector3(0, 0, 0);
    private final Vector3 rotate = new Vector3(0, 0, 0);
    private final Vector3 scale = new Vector3(1, 1, 1);

    public Vector3 getTranslate() {
        return translate;
    }

    public Vector3 getRotate() {
        return rotate;
    }

    public Vector3 getScale() {
        return scale;
    }

    @Override
    protected void reset() {
        translate.set(0, 0, 0);
        rotate.set(0, 0, 0);
        scale.set(1, 1, 1);
    }
}
