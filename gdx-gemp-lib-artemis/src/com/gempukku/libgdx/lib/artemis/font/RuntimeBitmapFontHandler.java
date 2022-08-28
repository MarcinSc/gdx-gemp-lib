package com.gempukku.libgdx.lib.artemis.font;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ObjectMap;

public class RuntimeBitmapFontHandler implements BitmapFontHandler {
    private ObjectMap<String, BitmapFont> loadedBitmapFonts = new ObjectMap<>();

    @Override
    public void setupWithWorld(World world) {

    }

    @Override
    public BitmapFont getBitmapFont(String path) {
        BitmapFont bitmapFont = loadedBitmapFonts.get(path);
        if (bitmapFont == null) {
            bitmapFont = new BitmapFont(Gdx.files.internal(path));
            loadedBitmapFonts.put(path, bitmapFont);
        }
        return bitmapFont;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void dispose() {
        for (BitmapFont value : loadedBitmapFonts.values()) {
            value.dispose();
        }
        loadedBitmapFonts.clear();
    }
}
