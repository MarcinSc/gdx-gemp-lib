package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.ui.curve.DefaultCurveDefinition;
import com.gempukku.libgdx.ui.curve.GCurveEditor;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.widget.VisTable;

public class CurveEditorPart extends VisTable implements GraphNodeEditorPart{
    private final DefaultCurveDefinition curveDefinition;
    private String property;

    public CurveEditorPart(String property, String styleName) {
        this.property = property;

        curveDefinition = new DefaultCurveDefinition();
        curveDefinition.addPoint(0, 0);

        final GCurveEditor curveEditor = new GCurveEditor(curveDefinition, styleName);
        curveEditor.setPrefWidth(300);
        curveEditor.setPrefHeight(200);

        curveEditor.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        curveEditor.fire(new GraphChangedEvent(false, true));
                    }
                });

        add(curveEditor).left().grow().row();
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
            curveDefinition.removePoint(0);
            JsonValue points = data.get(property);
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                curveDefinition.addPoint(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
            }
        }
    }

    @Override
    public void serializePart(JsonValue value) {
        Array<Vector2> points = curveDefinition.getPoints();
        JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
        for (Vector2 point : points) {
            pointsValue.addChild(new JsonValue(SimpleNumberFormatter.format(point.x) + "," + SimpleNumberFormatter.format(point.y)));
        }
        value.addChild(property, pointsValue);
    }
}
