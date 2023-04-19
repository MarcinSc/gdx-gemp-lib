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
import com.kotcrab.vis.ui.widget.VisTable;

public class GradientEditorPart extends VisTable implements GraphNodeEditorPart {
    private final DefaultGradientDefinition gradientDefinition;
    private String property;
    private GGradientEditor gradientEditor;

    public GradientEditorPart(String property, String styleName) {
        this.property = property;

        gradientDefinition = new DefaultGradientDefinition();
        gradientDefinition.addColor(0, Color.WHITE);

        gradientEditor = new GGradientEditor(gradientDefinition, styleName);

        gradientEditor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gradientEditor.fire(new GraphChangedEvent(false, true));
            }
        });

        add(gradientEditor).left().grow().row();
    }

    @Override
    public float getPrefWidth() {
        return 300;
    }

    @Override
    public float getPrefHeight() {
        return 40;
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
            gradientDefinition.removeColor(0);
            JsonValue points = data.get(property);
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                gradientDefinition.addColor(Float.parseFloat(split[1]), Color.valueOf(split[0]));
            }
        }
    }

    @Override
    public void serializePart(JsonValue value) {
        Array<GradientDefinition.ColorPosition> points = gradientEditor.getGradientDefinition().getColorPositions();
        JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
        for (GradientDefinition.ColorPosition point : points) {
            pointsValue.addChild(new JsonValue(point.color.toString() + "," + SimpleNumberFormatter.format(point.position)));
        }
        value.addChild(property, pointsValue);
    }
}
