package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class Vector2EditorPart extends VisTable implements GraphNodeEditorPart {
    private final String propertyX;
    private final String propertyY;
    private final VisValidatableTextField xInput;
    private final VisValidatableTextField yInput;

    public Vector2EditorPart(String label, String propertyX, String propertyY, float defaultValue) {
        this(label, propertyX, propertyY, defaultValue, null);
    }

    public Vector2EditorPart(String label, String propertyX, String propertyY,
                             float defaultValue, InputValidator inputValidator) {
        this(label, propertyX, propertyY, defaultValue, defaultValue, inputValidator, inputValidator);
    }

    public Vector2EditorPart(String label, String propertyX, String propertyY,
                             float defaultX, float defaultY,
                             InputValidator inputValidatorX, InputValidator inputValidatorY) {
        this(label, propertyX, propertyY, defaultX, defaultY, inputValidatorX, inputValidatorY, "default", "default");
    }

    public Vector2EditorPart(String label, String propertyX, String propertyY,
                             float defaultX, float defaultY,
                             InputValidator inputValidatorX, InputValidator inputValidatorY,
                             String labelStyleName, String textFieldStyleName) {
        this(label, propertyX, propertyY, defaultX, defaultY, inputValidatorX, inputValidatorY,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(textFieldStyleName, VisTextField.VisTextFieldStyle.class));
    }

    public Vector2EditorPart(String label, String propertyX, String propertyY,
                             float defaultX, float defaultY,
                             InputValidator inputValidatorX, InputValidator inputValidatorY,
                             Label.LabelStyle labelStyle, VisTextField.VisTextFieldStyle textFieldStyle) {
        this.propertyX = propertyX;
        this.propertyY = propertyY;
        xInput = VectorFieldCreator.createInput(inputValidatorX, defaultX, textFieldStyle);
        yInput = VectorFieldCreator.createInput(inputValidatorY, defaultY, textFieldStyle);

        add(new VisLabel(label, labelStyle));
        add(xInput).growX();
        add(yInput).growX();
        row();
    }

    public Vector2 getValue() {
        return new Vector2(Float.parseFloat(xInput.getText()), Float.parseFloat(yInput.getText()));
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
            float valueX = data.getFloat(propertyX);
            float valueY = data.getFloat(propertyY);
            xInput.setText(String.valueOf(valueX));
            yInput.setText(String.valueOf(valueY));
        }
    }

    public void setValue(float x, float y) {
        xInput.setText(String.valueOf(x));
        yInput.setText(String.valueOf(y));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(propertyX, new JsonValue(Float.parseFloat(xInput.getText())));
        object.addChild(propertyY, new JsonValue(Float.parseFloat(yInput.getText())));
    }
}
