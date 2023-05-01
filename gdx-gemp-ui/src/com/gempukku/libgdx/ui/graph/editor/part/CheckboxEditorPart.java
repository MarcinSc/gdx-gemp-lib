package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class CheckboxEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final VisCheckBox input;

    private boolean oldValue;

    public CheckboxEditorPart(String label, String property) {
        this(label, property, false);
    }

    public CheckboxEditorPart(String label, String property, boolean defaultValue) {
        this(label, property, defaultValue, "default");
    }

    public CheckboxEditorPart(String label, String property, boolean defaultValue, String checkBoxStyleName) {
        this(label, property, defaultValue, VisUI.getSkin().get(checkBoxStyleName, VisCheckBox.VisCheckBoxStyle.class));
    }

    public CheckboxEditorPart(String label, String property, boolean defaultValue, VisCheckBox.VisCheckBoxStyle style) {
        this.property = property;
        this.oldValue = defaultValue;

        input = new VisCheckBox(label, style);
        input.setChecked(defaultValue);
        input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        boolean newValue = input.isChecked();
                        if (newValue != oldValue) {
                            UndoableChangeEvent undoableEvent = Pools.obtain(UndoableChangeEvent.class);
                            undoableEvent.setUndoableAction(new SetSelectedAction(oldValue, newValue));
                            input.fire(undoableEvent);
                            Pools.free(undoableEvent);
                            oldValue = newValue;
                        }

                        event.stop();
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

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(input.isChecked()));
    }

    private class SetSelectedAction extends DefaultUndoableAction {
        private boolean oldValue;
        private boolean newValue;

        public SetSelectedAction(boolean oldValue, boolean newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void undoAction() {
            input.setChecked(oldValue);
        }

        @Override
        public void redoAction() {
            input.setChecked(newValue);
        }
    }
}
