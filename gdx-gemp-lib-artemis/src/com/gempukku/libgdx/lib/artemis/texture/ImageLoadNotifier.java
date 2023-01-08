package com.gempukku.libgdx.lib.artemis.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface ImageLoadNotifier {
    void textureLoaded(TextureRegion textureRegion);

    void textureError();
}
