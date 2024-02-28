package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.undo.UndoableValidatableTextField;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class IntegerEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final VisValidatableTextField v1Input;

    public IntegerEditorPart(String label, String property, int defaultValue) {
        this(label, property, defaultValue, null);
    }

    public IntegerEditorPart(String label, String property, int defaultValue, InputValidator inputValidator) {
        this(label, property, defaultValue, inputValidator, "default", "default");
    }

    public IntegerEditorPart(String label, String property, int defaultValue, InputValidator inputValidator,
                             String labelStyleName, String textFieldStyleName) {
        this(label, property, defaultValue, inputValidator,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(textFieldStyleName, VisTextField.VisTextFieldStyle.class));
    }

    public IntegerEditorPart(String label, String property, int defaultValue, InputValidator inputValidator,
                             Label.LabelStyle labelStyle, VisTextField.VisTextFieldStyle textFieldStyle) {
        this.property = property;
        v1Input = new UndoableValidatableTextField(String.valueOf(defaultValue), textFieldStyle) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.addValidator(Validators.INTEGERS);
        if (inputValidator != null)
            v1Input.addValidator(inputValidator);
        v1Input.setRestoreLastValid(true);
        v1Input.setAlignment(Align.right);

        add(new VisLabel(label, labelStyle));
        add(v1Input).growX();
        row();
    }

    public int getValue() {
        return Integer.parseInt(v1Input.getText());
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
            int value = data.getInt(property);
            v1Input.setText(String.valueOf(value));
        }
    }

    public void setValue(float value) {
        v1Input.setText(String.valueOf(value));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(Integer.parseInt(v1Input.getText())));
    }
}
