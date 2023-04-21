package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.awt.*;

public class SectionEditorPart extends VisTable implements GraphNodeEditorPart {
    public SectionEditorPart(String sectionLabel) {
        this(sectionLabel, "default", "default");
    }

    public SectionEditorPart(String sectionLabel, String labelStyleName, String separatorStyleName) {
        this(sectionLabel,
                VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class),
                VisUI.getSkin().get(separatorStyleName, Separator.SeparatorStyle.class));
    }

    public SectionEditorPart(String sectionLabel, Label.LabelStyle labelStyle, Separator.SeparatorStyle separatorStyle) {
        Separator separator = new Separator(separatorStyle);
        add(separator).growX().row();
        VisLabel label = new VisLabel(sectionLabel, labelStyle);
        label.setColor(Color.GRAY);
        label.setAlignment(Align.center);
        add(label).growX().row();
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

    }

    @Override
    public void serializePart(JsonValue object) {

    }
}
