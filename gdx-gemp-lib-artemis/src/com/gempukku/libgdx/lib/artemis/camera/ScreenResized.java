package com.gempukku.libgdx.lib.artemis.camera;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class ScreenResized implements EntityEvent {
    private int width;
    private int height;

    public ScreenResized(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
