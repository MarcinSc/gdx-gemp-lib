package com.gempukku.libgdx.lib.artemis.font;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontSystem extends BaseSystem {
    private BitmapFontHandler bitmapFontHandler;

    public BitmapFontSystem(BitmapFontHandler textureHandler) {
        this.bitmapFontHandler = textureHandler;
    }

    @Override
    protected void initialize() {
        bitmapFontHandler.setupWithWorld(world);
    }

    public BitmapFont getBitmapFont(String path) {
        return bitmapFontHandler.getBitmapFont(path);
    }

    @Override
    protected void processSystem() {
        bitmapFontHandler.update(world.getDelta());
    }

    @Override
    public void dispose() {
        bitmapFontHandler.dispose();
    }
}
