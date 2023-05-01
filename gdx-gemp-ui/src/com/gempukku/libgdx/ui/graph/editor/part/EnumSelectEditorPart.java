package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class EnumSelectEditorPart<T extends Enum<T>> extends VisTable implements GraphNodeEditorPart {
    private final VisSelectBox<String> selectBox;
    private final String[] resultValues;

    private final String property;

    private String oldValue;

    private static String[] convertToStrings(Array<? extends Enum<?>> values) {
        String[] result = new String[values.size];
        for (int i = 0; i < values.size; i++) {
            result[i] = values.get(i).name();
        }
        return result;
    }

    private static <T extends Enum<T>> String[] convertToDisplayText(Function<T, String> displayTextFunction, Array<T> values) {
        String[] result = new String[values.size];
        for (int i = 0; i < values.size; i++) {
            result[i] = displayTextFunction.evaluate(values.get(i));
        }
        return result;
    }

    public EnumSelectEditorPart(String label, String property, Function<T, String> displayTextFunction, Array<T> values) {
        this(label, property, values.get(0), displayTextFunction, "default", "default", values);
    }

    public EnumSelectEditorPart(String label, String property, T selectedValue, Function<T, String> displayTextFunction, Array<T> values) {
        this(label, property, selectedValue, displayTextFunction, "default", "default", values);
    }

    public EnumSelectEditorPart(String label, String property, T selectedValue, Function<T, String> displayTextFunction,
                                String labelStyleName, String selectBoxStyleName, Array<T> values) {
        this(label, property, selectedValue, displayTextFunction,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(selectBoxStyleName, SelectBox.SelectBoxStyle.class),
                values);
    }

    public EnumSelectEditorPart(String label, String property, T selectedValue, Function<T, String> displayTextFunction,
                                Label.LabelStyle labelStyle, SelectBox.SelectBoxStyle selectBoxStyle,  Array<T> values) {
        this.property = property;
        this.oldValue = displayTextFunction.evaluate(selectedValue);

        selectBox = new VisSelectBox<>(selectBoxStyle);
        selectBox.setItems(convertToDisplayText(displayTextFunction, values));
        selectBox.setSelected(oldValue);
        add(new VisLabel(label + " ", labelStyle));
        add(selectBox).growX();
        row();

        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        String value = selectBox.getSelected();
                        if (!oldValue.equals(value)) {
                            UndoableChangeEvent undoableEvent = Pools.obtain(UndoableChangeEvent.class);
                            undoableEvent.setUndoableAction(new SetSelectedAction(oldValue, value));
                            fire(undoableEvent);
                            Pools.free(undoableEvent);
                            oldValue = value;
                        }
                    }
                });
        resultValues = convertToStrings(values);
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

    public String getSelected() {
        return resultValues[selectBox.getSelectedIndex()];
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(getSelected()));
    }

    private class SetSelectedAction extends DefaultUndoableAction {
        private final String oldValue;
        private final String newValue;

        public SetSelectedAction(String oldValue, String newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void undoAction() {
            selectBox.setSelected(oldValue);
        }

        @Override
        public void redoAction() {
            selectBox.setSelected(newValue);
        }
    }
}
