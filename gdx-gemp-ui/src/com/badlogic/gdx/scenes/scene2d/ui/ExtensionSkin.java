package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;

public class ExtensionSkin extends Skin {
    public ExtensionSkin() {
        super();
    }

    public ExtensionSkin(FileHandle skinFile) {
        super(skinFile);
    }

    public ExtensionSkin(FileHandle skinFile, TextureAtlas atlas) {
        super(skinFile, atlas);
    }

    public ExtensionSkin(TextureAtlas atlas) {
        super(atlas);
    }

    public void mergeInto(Skin skin) {
        for (ObjectMap.Entry<Class, ObjectMap<String, Object>> classResources : this.resources.entries()) {
            ObjectMap<String, Object> output = skin.resources.get(classResources.key);
            if (output == null) {
                output = new ObjectMap<>();
                skin.resources.put(classResources.key, output);
            }
            output.putAll(classResources.value);
        }
    }
}
