package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class CheckboxEditorPart extends VisTable implements GraphNodeEditorPart {
    private String property;
    private final VisCheckBox input;

    public CheckboxEditorPart(String label, String property) {
        this(label, property, false);
    }

    public CheckboxEditorPart(String label, String property, boolean selected) {
        this.property = property;

        input = new VisCheckBox(label);
        input.setChecked(selected);

        add(input).left().grow();
        row();
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

    public void initialize(JsonValue data) {
        if (data != null) {
            input.setChecked(data.getBoolean(property, false));
        }
    }

    public void setValue(boolean value) {
        input.setChecked(value);
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(input.isChecked()));
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph graph) {

    }

    @Override
    public void dispose() {

    }
}
