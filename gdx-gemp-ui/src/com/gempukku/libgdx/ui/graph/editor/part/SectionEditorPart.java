package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class SectionEditorPart extends VisTable implements GraphNodeEditorPart {
    private Separator separator = new Separator();

    public SectionEditorPart(String sectionLabel) {
        add(separator).growX().row();
        VisLabel label = new VisLabel(sectionLabel);
        label.setColor(Color.GRAY);
        label.setAlignment(Align.center);
        add(label).growX().row();
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
    public Actor getActor() {
        return this;
    }

    @Override
    public void initialize(JsonValue data) {

    }

    @Override
    public void serializePart(JsonValue object) {

    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph graph) {

    }

    @Override
    public void dispose() {

    }
}
