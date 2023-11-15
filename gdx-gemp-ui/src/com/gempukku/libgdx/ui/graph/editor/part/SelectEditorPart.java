package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.undo.UndoableSelectBox;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class SelectEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final VisSelectBox<String> selectBox;

    public SelectEditorPart(String label, String property, String... values) {
        this(label, property, "default", "default", values);
    }

    public SelectEditorPart(String label, String property, String labelStyleName, String selectBoxStyleName, String... values) {
        this(label, property,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(selectBoxStyleName, SelectBox.SelectBoxStyle.class),
                values);
    }

    public SelectEditorPart(String label, String property, Label.LabelStyle labelStyle, SelectBox.SelectBoxStyle selectBoxStyle, String... values) {
        this.property = property;

        selectBox = new UndoableSelectBox<>(selectBoxStyle);
        selectBox.setItems(values);
        add(new VisLabel(label + " ", labelStyle));
        add(selectBox).growX();
        row();
    }

    public String getSelected() {
        return selectBox.getSelected();
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
            String value = data.getString(property, null);
            if (value != null)
                selectBox.setSelected(value);
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(getSelected()));
    }
}
