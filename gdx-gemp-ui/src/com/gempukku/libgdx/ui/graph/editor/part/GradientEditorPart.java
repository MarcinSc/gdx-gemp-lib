package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.ui.gradient.DefaultGradientDefinition;
import com.gempukku.libgdx.ui.gradient.GGradientEditor;
import com.gempukku.libgdx.ui.gradient.GradientDefinition;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class GradientEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final GGradientEditor gradientEditor;

    public GradientEditorPart(String property) {
        this(property, "default");
    }

    public GradientEditorPart(String property, String gradientEditorStyleName) {
        this(property, VisUI.getSkin().get(gradientEditorStyleName, GGradientEditor.GGradientEditorStyle.class));
    }

    public GradientEditorPart(String property, GGradientEditor.GGradientEditorStyle gradientEditorStyle) {
        this.property = property;

        gradientEditor = new GGradientEditor(new DefaultGradientDefinition(), gradientEditorStyle);

        add(gradientEditor).width(300).height(40).left().grow().row();
    }

    public GradientDefinition getGradientDefinition() {
        return new DefaultGradientDefinition(gradientEditor.getGradientDefinition());
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
    public Actor getActor() {
        return this;
    }

    @Override
    public void initialize(JsonValue data) {
        if (data != null) {
            Array<GradientDefinition.ColorPosition> colors = new Array<>();
            JsonValue points = data.get(property);
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                colors.add(new GradientDefinition.ColorPosition(Float.parseFloat(split[1]), Color.valueOf(split[0])));
            }
            gradientEditor.setGradientDefinition(new DefaultGradientDefinition(colors));
        }
    }

    @Override
    public void serializePart(JsonValue value) {
        Iterable<GradientDefinition.ColorPosition> points = gradientEditor.getGradientDefinition().getColorPositions();
        JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
        for (GradientDefinition.ColorPosition point : points) {
            pointsValue.addChild(new JsonValue(point.color.toString() + "," + SimpleNumberFormatter.format(point.position)));
        }
        value.addChild(property, pointsValue);
    }
}
