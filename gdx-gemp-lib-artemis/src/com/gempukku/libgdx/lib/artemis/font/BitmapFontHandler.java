package com.gempukku.libgdx.lib.artemis.font;

import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public interface BitmapFontHandler extends Disposable {
    void setupWithWorld(World world);

    BitmapFont getBitmapFont(String path);

    void update(float deltaTime);
}
