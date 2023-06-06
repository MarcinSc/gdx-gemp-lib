package com.gempukku.libgdx.fbx.handler.objects.animation;

import com.badlogic.gdx.utils.Array;

public class FbxAnimationLayer {
    private Array<Object> values;

    public FbxAnimationLayer(Array<Object> values) {
        this.values = values;
    }

    public Array<Object> getValues() {
        return values;
    }
}
