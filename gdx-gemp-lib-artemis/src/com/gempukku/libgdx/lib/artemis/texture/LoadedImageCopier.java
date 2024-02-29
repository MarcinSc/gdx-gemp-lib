package com.gempukku.libgdx.lib.artemis.texture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface LoadedImageCopier {
    TextureRegion copyPixmapToTexture(Pixmap pixmap, Texture texture, int x, int y, int cellWidth, int cellHeight);
}
