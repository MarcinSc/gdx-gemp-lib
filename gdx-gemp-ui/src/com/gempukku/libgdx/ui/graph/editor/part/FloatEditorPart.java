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

public class FloatEditorPart extends VisTable implements GraphNodeEditorPart {
    private String property;
    private final VisValidatableTextField v1Input;

    public FloatEditorPart(String label, String property, float defaultValue, InputValidator inputValidator) {
        this.property = property;
        if (inputValidator != null)
            v1Input = new VisValidatableTextField(Validators.FLOATS, inputValidator) {
                @Override
                public float getPrefWidth() {
                    return 50;
                }
            };
        else
            v1Input = new VisValidatableTextField(Validators.FLOATS) {
                @Override
                public float getPrefWidth() {
                    return 50;
                }
            };
        v1Input.setText(String.valueOf(defaultValue));
        v1Input.setAlignment(Align.right);
        v1Input.setRestoreLastValid(true);
        v1Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (v1Input.isInputValid()) {
                            v1Input.fire(new GraphChangedEvent(false, true));
                        }
                    }
                });

        add(new VisLabel(label));
        add(v1Input).growX();
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
            float value = data.getFloat(property);
            v1Input.setText(String.valueOf(value));
        }
    }

    public void setValue(float value) {
        v1Input.setText(String.valueOf(value));
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(Float.parseFloat(v1Input.getText())));
    }
}
