package com.gempukku.libgdx.camera2d;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import org.junit.BeforeClass;

public abstract class LibGDXTest {
    protected static final float TEST_DELTA = 0.001f;

    @BeforeClass
    public static void initNatives() {
        Lwjgl3NativesLoader.load();
        Gdx.files = new Lwjgl3Files();
        Gdx.app = new Lwjgl3Application(new ApplicationAdapter() {
        });
    }
}
