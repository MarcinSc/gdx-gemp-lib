package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class IndexEditorPart extends VisTable implements GraphNodeEditorPart {
    private final VisValidatableTextField indexField;

    private String property;

    public IndexEditorPart(String label, String property) {
        this(label, property, "default", "default");
    }

    public IndexEditorPart(String label, String property, String labelStyleName, String textFieldStyleName) {
        this(label, property,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(textFieldStyleName, VisTextField.VisTextFieldStyle.class));
    }

    public IndexEditorPart(String label, String property, Label.LabelStyle labelStyle, VisTextField.VisTextFieldStyle textFieldStyle) {
        super();
        this.property = property;

        indexField = new VisValidatableTextField("0", textFieldStyle);
        indexField.addValidator(Validators.INTEGERS);
        indexField.addValidator(new Validators.GreaterThanValidator(0, true));
        indexField.setRestoreLastValid(true);
        add(new VisLabel(label + " ", labelStyle));
        add(indexField).growX();
        row();

        indexField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (indexField.isInputValid()) {
                            fire(new GraphChangedEvent(false, true));
                        }
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
            int value = data.getInt(property);
            indexField.setText(String.valueOf(value));
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(Integer.parseInt(indexField.getText())));
    }
}
