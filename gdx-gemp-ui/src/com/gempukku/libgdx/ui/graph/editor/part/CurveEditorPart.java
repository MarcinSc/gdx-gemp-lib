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
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class CurveEditorPart extends VisTable implements GraphNodeEditorPart{
    private final String property;
    private final GCurveEditor curveEditor;

    public CurveEditorPart(String property) {
        this(property, "default");
    }

    public CurveEditorPart(String property, String curveEditorStyleName) {
        this(property, VisUI.getSkin().get(curveEditorStyleName, GCurveEditor.GCurveEditorStyle.class));
    }

    public CurveEditorPart(String property, GCurveEditor.GCurveEditorStyle curveEditorStyle) {
        this.property = property;

        curveEditor = new GCurveEditor(new DefaultCurveDefinition(), curveEditorStyle);

        add(curveEditor).width(300).height(200).left().grow().row();
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
            Array<Vector2> pointsArray = new Array<>();
            JsonValue points = data.get(property);
            for (String point : points.asStringArray()) {
                String[] split = point.split(",");
                pointsArray.add(new Vector2(Float.parseFloat(split[0]), Float.parseFloat(split[1])));
            }
            curveEditor.setCurveDefinition(new DefaultCurveDefinition(pointsArray));
        }
    }

    @Override
    public void serializePart(JsonValue value) {
        Iterable<Vector2> points = curveEditor.getCurveDefinition().getPoints();
        JsonValue pointsValue = new JsonValue(JsonValue.ValueType.array);
        for (Vector2 point : points) {
            pointsValue.addChild(new JsonValue(SimpleNumberFormatter.format(point.x) + "," + SimpleNumberFormatter.format(point.y)));
        }
        value.addChild(property, pointsValue);
    }
}
