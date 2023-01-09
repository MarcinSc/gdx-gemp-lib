package com.gempukku.libgdx.lib.artemis.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public interface TextureHandler extends Disposable {
    TextureRegion getTextureRegion(String atlas, String region);
}
