package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class CheckboxEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final VisCheckBox input;

    public CheckboxEditorPart(String label, String property) {
        this(label, property, false);
    }

    public CheckboxEditorPart(String label, String property, boolean selected) {
        this(label, property, selected, "default");
    }

    public CheckboxEditorPart(String label, String property, boolean selected, String checkBoxStyleName) {
        this(label, property, selected, VisUI.getSkin().get(checkBoxStyleName, VisCheckBox.VisCheckBoxStyle.class));
    }

    public CheckboxEditorPart(String label, String property, boolean selected, VisCheckBox.VisCheckBoxStyle style) {
        this.property = property;

        input = new VisCheckBox(label, style);
        input.setChecked(selected);
        input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        input.fire(new GraphChangedEvent(false, true));
                    }
                });

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

    @Override
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
}
