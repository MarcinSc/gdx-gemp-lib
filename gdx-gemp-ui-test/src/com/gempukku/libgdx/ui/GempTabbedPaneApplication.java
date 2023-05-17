package com.gempukku.libgdx.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.ui.collapse.CollapsibleContainer;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.undo.*;
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
import com.gempukku.libgdx.ui.graph.editor.part.GradientEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.IntegerEditorPart;
import com.gempukku.libgdx.ui.preview.PreviewWidget;
import com.gempukku.libgdx.ui.tabbedpane.GTabbedPane;
import com.gempukku.libgdx.undo.event.UndoableEvent;
import com.gempukku.libgdx.undo.event.UndoableListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;

public class GempTabbedPaneApplication extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private ColorPicker colorPicker;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skin/visui/uiskin.json"));
        VisUI.load(skin);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        colorPicker = new ColorPicker();

        Supplier<ColorPicker> colorPickerSupplier = new Supplier<ColorPicker>() {
            @Override
            public ColorPicker get() {
                return colorPicker;
            }
        };

        GTabbedPane tabbedPane = new GTabbedPane();
        TestTab tab1 = createCurveTestTab(tabbedPane);
        TestTab tab2 = createGradientTestTab(tabbedPane, colorPickerSupplier);
        TestDirtyTab tab3 = createDirtyTestTab(tabbedPane);

        GraphEditor graphEditor = createGraphEditor("Whatever", colorPickerSupplier);

        TestTab tab4 = createGraphTestTab(tabbedPane, graphEditor);

        TestTab tab5 = createPreviewTestTab(tabbedPane, graphEditor);

        tabbedPane.addTab(tab1);
        tabbedPane.addTab(tab2);
        tabbedPane.addTab(tab3);
        tabbedPane.addTab(tab4);
        tabbedPane.addTab(tab5);

        tabbedPane.setFillParent(true);
        stage.addActor(tabbedPane);
    }

    private static TestTab createGraphTestTab(GTabbedPane tabbedPane, GraphEditor graphEditor) {
        DefaultUndoManager undoManager = new DefaultUndoManager();
        VisTable buttonTable = new VisTable();
        VisTextButton undo = new VisTextButton("Undo");
        undo.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (undoManager.canUndo()) {
                            undoManager.undo();
                        }
                    }
                });
        VisTextButton redo = new VisTextButton("Redo");
        redo.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (undoManager.canRedo()) {
                            undoManager.redo();
                        }
                    }
                });

        buttonTable.add(undo);
        buttonTable.add(redo);

        graphEditor.addListener(
                new UndoableListener() {
                    @Override
                    public void undoable(UndoableEvent event) {
                        UndoableAction undoableAction = event.getUndoableAction();
                        if (undoableAction != null)
                            undoManager.addUndoableAction(undoableAction);
                    }
                });

        TestTab tab4 = new TestTab(tabbedPane, "Graph", true);
        tab4.add(buttonTable).colspan(3).row();
        tab4.add(new VisLabel("Left"));
        tab4.add(graphEditor).grow();
        tab4.add(new VisLabel("Right")).row();
        tab4.add(new VisLabel("Bottom")).colspan(3).row();
        return tab4;
    }

    private static TestTab createPreviewTestTab(GTabbedPane tabbedPane, GraphEditor graphEditor) {
        TestTab tab5 = new TestTab(tabbedPane, "Preview", true);
        tab5.add(new VisLabel("Top")).colspan(3).row();
        tab5.add(new VisLabel("Left"));
        tab5.add(new PreviewWidget(graphEditor)).grow();
        tab5.add(new VisLabel("Right")).row();
        tab5.add(new VisLabel("Bottom")).colspan(3).row();
        return tab5;
    }

    private static TestDirtyTab createDirtyTestTab(GTabbedPane tabbedPane) {
        final TestDirtyTab tab3 = new TestDirtyTab(tabbedPane, "Dirty", true);
        final VisCheckBox dirtyCheckbox = new VisCheckBox("Dirty", false);
        tab3.add(dirtyCheckbox);
        return tab3;
    }

    private static TestTab createGradientTestTab(GTabbedPane tabbedPane, Supplier<ColorPicker> colorPickerSupplier) {
        TestTab tab2 = new TestTab(tabbedPane, "Gradient", true);
        tab2.add(new VisLabel("Top")).colspan(3).row();
        tab2.add(new VisLabel("Left"));

        DefaultGradientDefinition gradientDefinition = new DefaultGradientDefinition(
                new Array<>(new GradientDefinition.ColorPosition[]{
                        new GradientDefinition.ColorPosition(0, Color.WHITE),
                        new GradientDefinition.ColorPosition(0.3f, Color.BLUE),
                        new GradientDefinition.ColorPosition(0.8f, Color.BLACK)}));

        GGradientEditor gradientEditor = new GGradientEditor(colorPickerSupplier, gradientDefinition);
        tab2.add(gradientEditor).grow();
        tab2.add(new VisLabel("Right")).row();
        tab2.add(new VisLabel("Bottom")).colspan(3).row();
        return tab2;
    }

    private static TestTab createCurveTestTab(GTabbedPane tabbedPane) {
        TestTab tab1 = new TestTab(tabbedPane, "Curve", false);
        tab1.add(new VisLabel("Top")).colspan(3).row();
        tab1.add(new VisLabel("Left"));
        DefaultCurveDefinition curveDefinition = new DefaultCurveDefinition(
                new Array<>(new Vector2[]{
                        new Vector2(0.1f, 0),
                        new Vector2(0.3f, 1),
                        new Vector2(0.7f, 0.5f),
                        new Vector2(0.9f, 0)}));

        GCurveEditor curveEditor = new GCurveEditor(curveDefinition);
        curveEditor.addListener(
                new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        if (event instanceof UndoableEvent) {
                            UndoableEvent undoableEvent = (UndoableEvent) event;
                            if (undoableEvent.getUndoableAction() != null) {
                                System.out.println("Undoable action");
                            }
                            return true;
                        }
                        return false;
                    }
                });
        tab1.add(curveEditor).grow();
        tab1.add(new VisLabel("Right")).row();
        tab1.add(new VisLabel("Bottom")).colspan(3).row();
        return tab1;
    }

    private static GraphEditor createGraphEditor(String type, Supplier<ColorPicker> colorPickerSupplier) {
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
                        protected Drawable getInputDrawable(GraphNodeInputSide side, boolean valid) {
                            return VisUI.getSkin().getDrawable(valid ? "white" : "darkGrey");
                        }

                        @Override
                        protected Drawable getOutputDrawable(GraphNodeOutputSide side, boolean valid) {
                            return VisUI.getSkin().getDrawable(valid ? "white" : "darkGrey");
                        }

                        @Override
                        protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
                            graphNodeEditor.addGraphBoxPart(
                                    new GradientEditorPart(colorPickerSupplier, "gradient", "default"));
                            graphNodeEditor.addGraphBoxPart(new IntegerEditorPart("Value", "prop", 0, new Validators.IntegerValidator()));
                        }
                    };

                    return producer;
                } else if (s.equals("intIn")) {
                    DefaultGraphNodeEditorProducer producer = new DefaultGraphNodeEditorProducer(intIn) {
                        @Override
                        protected Drawable getInputDrawable(GraphNodeInputSide side, boolean valid) {
                            return VisUI.getSkin().getDrawable(valid ? "white" : "darkGrey");
                        }

                        @Override
                        protected Drawable getOutputDrawable(GraphNodeOutputSide side, boolean valid) {
                            return VisUI.getSkin().getDrawable(valid ? "white" : "darkGrey");
                        }

                        @Override
                        protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
                            graphNodeEditor.addGraphBoxPart(
                                    new CurveEditorPart("curve", "default"));
                            CollapsibleContainer container = new CollapsibleContainer(new VisLabel("Collapsible label"), "toggle");
                            graphNodeEditor.addGraphBoxPart(
                                    new DefaultGraphNodeEditorPart(container, null));
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
        colorPicker.dispose();
        stage.dispose();
        VisUI.dispose();
        skin.dispose();
    }
}
