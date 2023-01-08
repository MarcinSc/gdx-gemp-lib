package com.gempukku.libgdx.lib.artemis.texture;

import com.gempukku.libgdx.lib.artemis.evaluate.EvaluableProperty;

public class TextureReference implements EvaluableProperty {
    private String atlas;
    private String region;

    public String getAtlas() {
        return atlas;
    }

    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
