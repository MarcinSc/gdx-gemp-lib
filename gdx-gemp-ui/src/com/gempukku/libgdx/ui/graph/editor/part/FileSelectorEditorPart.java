package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class FileSelectorEditorPart extends VisTable implements GraphNodeEditorPart {
    private String property;
    private String selectedPath;

    public FileSelectorEditorPart(String label, String property) {
        this(label, property, null);
    }

    public FileSelectorEditorPart(String label, String property, String defaultPath) {
        this.property = property;
        this.selectedPath = defaultPath;

        add(new VisLabel(label)).growX();
        VisTextButton chooseFileButton = new VisTextButton("Choose");
        add(chooseFileButton);
        row();

        chooseFileButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        final FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                        fileChooser.setModal(true);
                        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                        fileChooser.setListener(new FileChooserAdapter() {
                            @Override
                            public void selected(Array<FileHandle> file) {
                                selectedPath = file.get(0).path();
                                fire(new GraphChangedEvent(false, true));
                            }
                        });
                        getStage().addActor(fileChooser.fadeIn());
                    }
                });
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    @Override
    public void initialize(JsonValue data) {
        if (data != null) {
            selectedPath = data.getString(property);
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(selectedPath));
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph graph) {

    }

    @Override
    public void dispose() {

    }
}
