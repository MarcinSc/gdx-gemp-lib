package com.gempukku.libgdx.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.ui.curve.DefaultCurveDefinition;
import com.gempukku.libgdx.ui.curve.GCurveEditor;
import com.gempukku.libgdx.ui.gradient.DefaultGradientDefinition;
import com.gempukku.libgdx.ui.gradient.GGradientEditor;
import com.gempukku.libgdx.ui.gradient.GradientDefinition;
import com.gempukku.libgdx.ui.tabbedpane.GTabbedPane;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

public class GempTabbedPaneApplication extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skin/visui/uiskin.json"));
        VisUI.load(skin);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        DefaultCurveDefinition curveDefinition = new DefaultCurveDefinition();
        curveDefinition.getPoints().add(new Vector2(0.1f, 0), new Vector2(0.3f, 1), new Vector2(0.7f, 0.5f), new Vector2(0.9f, 0));

        DefaultGradientDefinition gradientDefinition = new DefaultGradientDefinition();
        gradientDefinition.getColorPositions().add(
                new GradientDefinition.ColorPosition(0, Color.WHITE),
                new GradientDefinition.ColorPosition(0.3f, Color.BLUE),
                new GradientDefinition.ColorPosition(0.8f, Color.BLACK));

        GTabbedPane<TestTab> tabbedPane = new GTabbedPane<>();
        TestTab tab1 = new TestTab(tabbedPane, "Tab 1", false);
        tab1.add(new GCurveEditor(curveDefinition)).grow();
        TestTab tab2 = new TestTab(tabbedPane, "Tab 2", true);
        tab2.add(new GGradientEditor(gradientDefinition)).grow();
        TestTab tab3 = new TestTab(tabbedPane, "Tab 3", false);
        tab3.add(new VisLabel("Contents of tab 3"));
        tabbedPane.addTab(tab1);
        tabbedPane.addTab(tab2);
        tabbedPane.addTab(tab3);

        tabbedPane.setFillParent(true);
        stage.addActor(tabbedPane);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        VisUI.dispose();
        skin.dispose();
    }
}