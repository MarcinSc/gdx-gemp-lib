package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Function;

public class EnumSelectEditorPart<T extends Enum> extends VisTable implements GraphNodeEditorPart {
    private final VisSelectBox<String> selectBox;
    private Function<T, String> displayTextFunction;
    private String[] resultValues;

    private String property;

    private static String[] convertToStrings(Enum<?>[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].name();
        }
        return result;
    }

    private static <T extends Enum<T>> String[] convertToDisplayText(Function<T, String> displayTextFunction, T[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = displayTextFunction.apply(values[i]);
        }
        return result;
    }

    public EnumSelectEditorPart(String label, String property, Function<T, String> displayTextFunction, T... values) {
        this.property = property;
        selectBox = new VisSelectBox<>();
        selectBox.setItems(convertToDisplayText(displayTextFunction, values));
        add(new VisLabel(label + " "));
        add(selectBox).growX();
        row();

        selectBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(false, true));
                    }
                });
        this.displayTextFunction = displayTextFunction;
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

    public void initialize(JsonValue data) {
        if (data != null) {
            String value = data.getString(property, null);
            if (value != null)
                selectBox.setSelected(value);
        }
    }

    public void setEnabled(boolean enabled) {
        selectBox.setDisabled(!enabled);
        selectBox.setTouchable(enabled ? Touchable.enabled : Touchable.disabled);
    }

    public void setSelected(T value) {
        selectBox.setSelected(displayTextFunction.apply(value));
    }

    public String getSelected() {
        return resultValues[selectBox.getSelectedIndex()];
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(getSelected()));
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph graph) {

    }

    @Override
    public void dispose() {

    }
}
