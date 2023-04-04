package com.gempukku.libgdx.ui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class GempTabbedPaneTest {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640;
        config.height = 360;
        new LwjglApplication(new GempTabbedPaneApplication(), config);
    }
}
