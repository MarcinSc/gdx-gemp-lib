package com.gempukku.libgdx.lib.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.gempukku.libgdx.lib.test.scene.FacingLockedSceneCameraScene;
import com.gempukku.libgdx.lib.test.scene.LockedCameraScene;
import com.gempukku.libgdx.lib.test.scene.LockedSceneCameraScene;
import com.gempukku.libgdx.lib.test.scene.MultiplayerCameraScene;
import com.gempukku.libgdx.lib.test.scene.SnapCameraScene;
import com.gempukku.libgdx.lib.test.scene.SnapLimitCameraScene;

public class ReloadableLibTestApplication extends ApplicationAdapter {
    private LibgdxLibTestScene[] scenes;
    private int loadedIndex;
    private int width;
    private int height;
    private FPSLogger fpsLogger = new FPSLogger();

    private boolean profile = false;
    private GLProfiler profiler;
    private Skin profileSkin;
    private Stage profileStage;
    private Label profileLabel;

    public ReloadableLibTestApplication() {
        scenes = new LibgdxLibTestScene[]{
                new LockedCameraScene(),
                new LockedSceneCameraScene(),
                new FacingLockedSceneCameraScene(),
                new SnapCameraScene(),
                new SnapLimitCameraScene(),
                new MultiplayerCameraScene()
        };
        loadedIndex = 5;
    }

    @Override
    public void create() {
        //Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);

        scenes[loadedIndex].initializeScene();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        scenes[loadedIndex].resizeScene(width, height);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            scenes[loadedIndex].disposeScene();
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P) && loadedIndex > 0) {
            scenes[loadedIndex].disposeScene();
            loadedIndex--;
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && loadedIndex < scenes.length - 1) {
            scenes[loadedIndex].disposeScene();
            loadedIndex++;
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            if (profile)
                disableProfiler();
            else
                enableProfiler();
        }

        long start = 0;
        if (profile)
            fpsLogger.log();

        if (profile) {
            profiler.reset();
            start = System.nanoTime();
        }

        scenes[loadedIndex].renderScene();

        if (profile) {
            float ms = (System.nanoTime() - start) / 1000000f;

            StringBuilder sb = new StringBuilder();
            sb.append("Time: " + SimpleNumberFormatter.format(ms) + "ms\n");
            sb.append("GL Calls: " + profiler.getCalls() + "\n");
            sb.append("Draw calls: " + profiler.getDrawCalls() + "\n");
            sb.append("Shader switches: " + profiler.getShaderSwitches() + "\n");
            sb.append("Texture bindings: " + profiler.getTextureBindings() + "\n");
            sb.append("Vertex calls: " + profiler.getVertexCount().total + "\n");
            long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
            sb.append("Used memory: " + memory + "MB");
            profileLabel.setText(sb.toString());

            profileStage.draw();
        }
    }

    @Override
    public void dispose() {
        scenes[loadedIndex].disposeScene();

        if (profile) {
            profileSkin.dispose();
            profileStage.dispose();
        }

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }

    private void enableProfiler() {
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        profileSkin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        profileStage = new Stage();
        profileLabel = new Label("", profileSkin);

        Table tbl = new Table(profileSkin);

        tbl.setFillParent(true);
        tbl.align(Align.topRight);

        tbl.add(profileLabel).pad(10f);
        tbl.row();

        profileStage.addActor(tbl);

        profile = true;
    }

    private void disableProfiler() {
        profileSkin.dispose();
        profileStage.dispose();

        profiler.disable();

        profile = false;
    }
}
