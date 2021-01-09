package com.gempukku.libgdx.lib;

import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import org.junit.BeforeClass;

public abstract class LibGDXTest {
    @BeforeClass
    public static void initNatives() {
        LwjglNativesLoader.load();
    }
}
