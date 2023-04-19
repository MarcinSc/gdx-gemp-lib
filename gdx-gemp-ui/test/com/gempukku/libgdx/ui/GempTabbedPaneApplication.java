package com.gempukku.libgdx.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.ui.curve.DefaultCurveDefinition;
import com.gempukku.libgdx.ui.curve.GCurveEditor;
import com.gempukku.libgdx.ui.gradient.DefaultGradientDefinition;
import com.gempukku.libgdx.ui.gradient.GGradientEditor;
import com.gempukku.libgdx.ui.gradient.GradientDefinition;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.data.impl.*;
import com.gempukku.libgdx.ui.graph.GraphEditor;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.PopupMenuProducer;
import com.gempukku.libgdx.ui.graph.editor.part.CurveEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.IntegerEditorPart;
import com.gempukku.libgdx.ui.preview.NavigableCanvas;
import com.gempukku.libgdx.ui.preview.PreviewWidget;
import com.gempukku.libgdx.ui.tabbedpane.GTabbedPane;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisCheckBox;
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

        GTabbedPane tabbedPane = new GTabbedPane();
        TestTab tab1 = new TestTab(tabbedPane, "Curve", false);
        tab1.add(new VisLabel("Top")).colspan(3).row();
        tab1.add(new VisLabel("Left"));
        tab1.add(new GCurveEditor(curveDefinition)).grow();
        tab1.add(new VisLabel("Right")).row();
        tab1.add(new VisLabel("Bottom")).colspan(3).row();
        TestTab tab2 = new TestTab(tabbedPane, "Gradient", true);
        tab2.add(new VisLabel("Top")).colspan(3).row();
        tab2.add(new VisLabel("Left"));
        tab2.add(new GGradientEditor(gradientDefinition)).grow();
        tab2.add(new VisLabel("Right")).row();
        tab2.add(new VisLabel("Bottom")).colspan(3).row();

        final TestDirtyTab tab3 = new TestDirtyTab(tabbedPane, "Dirty", true);
        final VisCheckBox dirtyCheckbox = new VisCheckBox("Dirty", false);
        dirtyCheckbox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        tab3.setDirty(dirtyCheckbox.isChecked());
                    }
                });
        tab3.add(dirtyCheckbox);

        TestTab tab4 = new TestTab(tabbedPane, "Graph", true);
        tab4.add(new VisLabel("Top")).colspan(3).row();
        tab4.add(new VisLabel("Left"));
        GraphEditor graphEditor = createGraphEditor("Whatever");
        tab4.add(graphEditor).grow();
        tab4.add(new VisLabel("Right")).row();
        tab4.add(new VisLabel("Bottom")).colspan(3).row();

        TestTab tab5 = new TestTab(tabbedPane, "Preview", true);
        tab5.add(new VisLabel("Top")).colspan(3).row();
        tab5.add(new VisLabel("Left"));
        tab5.add(new PreviewWidget(graphEditor)).grow();
        tab5.add(new VisLabel("Right")).row();
        tab5.add(new VisLabel("Bottom")).colspan(3).row();

        tabbedPane.addTab(tab1);
        tabbedPane.addTab(tab2);
        tabbedPane.addTab(tab3);
        tabbedPane.addTab(tab4);
        tabbedPane.addTab(tab5);

        tabbedPane.setFillParent(true);
        stage.addActor(tabbedPane);
    }

    private static GraphEditor createGraphEditor(String type) {
        DefaultGraph<DefaultGraphNode, DefaultGraphConnection, DefaultNodeGroup> graph = new DefaultGraph<>(type);

        final DefaultNodeConfiguration intOut = new DefaultNodeConfiguration("intOut", "Integer Out");
        intOut.addNodeOutput(new DefaultGraphNodeOutput("0", "Value", "Int"));

        final DefaultNodeConfiguration intIn = new DefaultNodeConfiguration("intIn", "Integer In");
        intIn.addNodeInput(new DefaultGraphNodeInput("0", "Value", "Int"));

        graph.addGraphNode(new DefaultGraphNode("1", "intOut", 0, 0, null));
        graph.addGraphNode(new DefaultGraphNode("2", "intIn", 200, 0, null));

        ObjectSet<String> nodeIds = new ObjectSet<>();
        nodeIds.addAll("1", "2");
        graph.addNodeGroup(new DefaultNodeGroup("Group", nodeIds));

        Function<String, GraphNodeEditorProducer> graphNodeEditorProducers = new Function<String, GraphNodeEditorProducer>() {
            @Override
            public GraphNodeEditorProducer evaluate(String s) {
                if (s.equals("intOut")) {
                    DefaultGraphNodeEditorProducer producer = new DefaultGraphNodeEditorProducer(intOut) {
                        @Override
                        protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
                            graphNodeEditor.addGraphBoxPart(new IntegerEditorPart("Value", "prop", 0, new Validators.IntegerValidator()));
                        }
                    };

                    return producer;
                } else if (s.equals("intIn")) {
                    DefaultGraphNodeEditorProducer producer = new DefaultGraphNodeEditorProducer(intIn) {
                        @Override
                        protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
                            graphNodeEditor.addGraphBoxPart(
                                    new CurveEditorPart("curve", "default"));
                        }
                    };

                    return producer;
                }
                return null;
            }
        };
        PopupMenuProducer popupMenuProducer = null;
        GraphEditor graphEditor = new GraphEditor(graph, graphNodeEditorProducers, popupMenuProducer);
        graphEditor.centerCanvas();
        return graphEditor;
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
