package com.gempukku.libgdx.ui.graph.editor.part;

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

public class Vector4EditorPart extends VisTable implements GraphNodeEditorPart {
    private final String propertyX;
    private final String propertyY;
    private final String propertyZ;
    private final String propertyW;
    private final VisValidatableTextField xInput;
    private final VisValidatableTextField yInput;
    private final VisValidatableTextField zInput;
    private final VisValidatableTextField wInput;

    public Vector4EditorPart(String label, String propertyX, String propertyY, String propertyZ, String propertyW,
                             float defaultValue) {
        this(label, propertyX, propertyY, propertyZ, propertyW, defaultValue, null);
    }

    public Vector4EditorPart(String label, String propertyX, String propertyY, String propertyZ, String propertyW,
                             float defaultValue, InputValidator inputValidator) {
        this(label, propertyX, propertyY, propertyZ, propertyW, defaultValue, defaultValue, defaultValue, defaultValue,
                inputValidator, inputValidator, inputValidator, inputValidator);
    }

    public Vector4EditorPart(String label, String propertyX, String propertyY, String propertyZ, String propertyW,
                             float defaultX, float defaultY, float defaultZ, float defaultW,
                             InputValidator inputValidatorX, InputValidator inputValidatorY, InputValidator inputValidatorZ, InputValidator inputValidatorW) {
        this(label, propertyX, propertyY, propertyZ, propertyW, defaultX, defaultY, defaultZ, defaultW,
                inputValidatorX, inputValidatorY, inputValidatorZ, inputValidatorW,
                "default", "default");
    }

    public Vector4EditorPart(String label, String propertyX, String propertyY, String propertyZ, String propertyW,
                             float defaultX, float defaultY, float defaultZ, float defaultW,
                             InputValidator inputValidatorX, InputValidator inputValidatorY, InputValidator inputValidatorZ, InputValidator inputValidatorW,
                             String labelStyleName, String textFieldStyleName) {
        this(label, propertyX, propertyY, propertyZ, propertyW, defaultX, defaultY, defaultZ, defaultW,
                inputValidatorX, inputValidatorY, inputValidatorZ, inputValidatorW,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(textFieldStyleName, VisTextField.VisTextFieldStyle.class));
    }

    public Vector4EditorPart(String label, String propertyX, String propertyY, String propertyZ, String propertyW,
                             float defaultX, float defaultY, float defaultZ, float defaultW,
                             InputValidator inputValidatorX, InputValidator inputValidatorY, InputValidator inputValidatorZ, InputValidator inputValidatorW,
                             Label.LabelStyle labelStyle, VisTextField.VisTextFieldStyle textFieldStyle) {
        this.propertyX = propertyX;
        this.propertyY = propertyY;
        this.propertyZ = propertyZ;
        this.propertyW = propertyW;
        xInput = VectorFieldCreator.createInput(inputValidatorX, defaultX, textFieldStyle);
        yInput = VectorFieldCreator.createInput(inputValidatorY, defaultY, textFieldStyle);
        zInput = VectorFieldCreator.createInput(inputValidatorZ, defaultZ, textFieldStyle);
        wInput = VectorFieldCreator.createInput(inputValidatorW, defaultW, textFieldStyle);

        add(new VisLabel(label, labelStyle));
        add(xInput).growX();
        add(yInput).growX();
        add(zInput).growX();
        add(wInput).growX();
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
            float valueX = data.getFloat(propertyX);
            float valueY = data.getFloat(propertyY);
            float valueZ = data.getFloat(propertyZ);
            float valueW = data.getFloat(propertyW);
            xInput.setText(String.valueOf(valueX));
            yInput.setText(String.valueOf(valueY));
            zInput.setText(String.valueOf(valueZ));
            wInput.setText(String.valueOf(valueW));
        }
    }

    public void setValue(float x, float y, float z, float w) {
        xInput.setText(String.valueOf(x));
        yInput.setText(String.valueOf(y));
        zInput.setText(String.valueOf(z));
        wInput.setText(String.valueOf(w));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(propertyX, new JsonValue(Float.parseFloat(xInput.getText())));
        object.addChild(propertyY, new JsonValue(Float.parseFloat(yInput.getText())));
        object.addChild(propertyZ, new JsonValue(Float.parseFloat(zInput.getText())));
        object.addChild(propertyW, new JsonValue(Float.parseFloat(wInput.getText())));
    }
}
