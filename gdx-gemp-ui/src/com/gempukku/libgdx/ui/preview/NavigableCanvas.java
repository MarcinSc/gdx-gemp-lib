package com.gempukku.libgdx.ui.preview;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface NavigableCanvas {
    void getCanvasPosition(Vector2 result);

    void getCanvasSize(Vector2 result);

    void getVisibleSize(Vector2 result);

    void navigateTo(float x, float y);

    void processElements(Callback callback);

    interface Callback {
        void processElement(float x, float y, float width, float height);
    }
}
