package com.gempukku.libgdx.artemis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import org.junit.BeforeClass;

public abstract class LibGDXTest {
    @BeforeClass
    public static void initNatives() {
        LwjglNativesLoader.load();
        Gdx.files = new LwjglFiles();
        Gdx.app = new LwjglApplication(new ApplicationAdapter() {
        });
    }
}
