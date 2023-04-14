package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class Vector2EditorPart extends VisTable implements GraphNodeEditorPart {
    private String propertyX;
    private String propertyY;
    private final VisValidatableTextField xInput;
    private final VisValidatableTextField yInput;

    public Vector2EditorPart(String label, String propertyX, String propertyY,
                             float defaultX, float defaultY,
                             InputValidator inputValidatorX, InputValidator inputValidatorY) {
        this.propertyX = propertyX;
        this.propertyY = propertyY;
        xInput = createInput(inputValidatorX, defaultX);
        yInput = createInput(inputValidatorY, defaultY);

        add(new VisLabel(label));
        add(xInput).growX();
        add(yInput).growX();
        row();
    }

    private VisValidatableTextField createInput(InputValidator inputValidator, float defaultValue) {
        final VisValidatableTextField result;
        if (inputValidator != null) {
            result = new VisValidatableTextField(Validators.FLOATS, inputValidator) {
                @Override
                public float getPrefWidth() {
                    return 50;
                }
            };
        } else
            result = new VisValidatableTextField(Validators.FLOATS) {
                @Override
                public float getPrefWidth() {
                    return 50;
                }
            };
        result.setText(String.valueOf(defaultValue));
        result.setAlignment(Align.right);
        result.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        result.fire(new GraphChangedEvent(false, true));
                    }
                });
        return result;
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

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph graph) {

    }

    @Override
    public void dispose() {

    }
}
