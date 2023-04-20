package com.gempukku.libgdx.template;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import org.junit.BeforeClass;

public abstract class LibGDXTest {
    @BeforeClass
    public static void initNatives() {
        Lwjgl3NativesLoader.load();
        Gdx.files = new Lwjgl3Files();
        Gdx.app = new Lwjgl3Application(new ApplicationAdapter() {
        });
    }
}
