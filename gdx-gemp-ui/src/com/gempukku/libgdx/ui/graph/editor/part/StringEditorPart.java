package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.undo.UndoableTextField;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class StringEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final VisTextField input;

    public StringEditorPart(String label, String property) {
        this(label, property, "");
    }

    public StringEditorPart(String label, String property, String defaultValue) {
        this(label, property, defaultValue, "default", "default");
    }

    public StringEditorPart(String label, String property, String defaultValue, String labelStyleName, String textFieldStyleName) {
        this(label, property, defaultValue,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(textFieldStyleName, VisTextField.VisTextFieldStyle.class));
    }

    public StringEditorPart(String label, String property, String defaultValue, Label.LabelStyle labelStyle, VisTextField.VisTextFieldStyle textFieldStyle) {
        this.property = property;
        input = new UndoableTextField(defaultValue, textFieldStyle);

        add(new VisLabel(label, labelStyle));
        add(input).growX();
        row();
    }

    public String getText() {
        return input.getText();
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
            String value = data.getString(property);
            input.setText(value);
        }
    }

    public void setValue(String value) {
        input.setText(value);
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(input.getText()));
    }
}
