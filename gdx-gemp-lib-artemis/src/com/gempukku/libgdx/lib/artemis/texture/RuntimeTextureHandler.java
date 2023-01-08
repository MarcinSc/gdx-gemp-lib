package com.gempukku.libgdx.lib.artemis.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class RuntimeTextureHandler implements TextureHandler, Disposable {
    private ObjectMap<String, TextureAtlas> loadedTextures = new ObjectMap<>();

    @Override
    public TextureRegion getTextureRegion(String atlas, String region) {
        TextureAtlas textureAtlas = loadedTextures.get(atlas);
        if (textureAtlas == null) {
            if (atlas.endsWith(".atlas")) {
                textureAtlas = new TextureAtlas(atlas);
                loadedTextures.put(atlas, textureAtlas);
            } else {
                textureAtlas = new TextureAtlas();
                Texture texture = new Texture(atlas);
                textureAtlas.addRegion(region, texture, 0, 0, texture.getWidth(), texture.getHeight());
                loadedTextures.put(atlas, textureAtlas);
            }
        }

        return textureAtlas.findRegion(region);
    }

    @Override
    public void dispose() {
        for (TextureAtlas value : loadedTextures.values()) {
            value.dispose();
        }
        loadedTextures.clear();
    }
}
