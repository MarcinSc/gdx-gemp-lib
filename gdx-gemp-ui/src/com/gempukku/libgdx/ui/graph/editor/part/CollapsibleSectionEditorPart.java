package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.collapse.CollapsibleContainer;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisTable;

public class CollapsibleSectionEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final GraphNodeEditorPart child;
    private final CollapsibleContainer collapsibleContainer;

    private final VisImageTextButton expandButton;

    public CollapsibleSectionEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart, String sectionLabel) {
        this(property, graphNodeEditorPart, sectionLabel, "default", "default");
    }

    public CollapsibleSectionEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart, String sectionLabel, String imageTextButtonStyleName, String separatorStyleName) {
        this(property, graphNodeEditorPart, sectionLabel,
                VisUI.getSkin().get(imageTextButtonStyleName, VisImageTextButton.VisImageTextButtonStyle.class),
                VisUI.getSkin().get(separatorStyleName, Separator.SeparatorStyle.class));
    }

    public CollapsibleSectionEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart, String sectionLabel,
                                        VisImageTextButton.VisImageTextButtonStyle imageTextButtonStyle, Separator.SeparatorStyle separatorStyle) {
        this.property = property;
        this.child = graphNodeEditorPart;
        this.collapsibleContainer = new CollapsibleContainer(child.getActor());

        Separator separator = new Separator(separatorStyle);
        add(separator).growX().row();

        expandButton = new VisImageTextButton(sectionLabel, imageTextButtonStyle);
        expandButton.align(Align.left);
        expandButton.setFocusBorderEnabled(false);
        expandButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        boolean expand = expandButton.isChecked();
                        collapsibleContainer.setExpanded(expand);
                        event.stop();
                    }
                });
        add(expandButton).left().growX().row();
        add(collapsibleContainer).growX().row();
    }

    @Override
    public GraphNodeEditorOutput getOutputConnector() {
        return child.getOutputConnector();
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return child.getInputConnector();
    }

    @Override
    public Actor getActor() {
        return this;
    }

    public void setExpanded(boolean expanded) {
        expandButton.setChecked(expanded);
    }

    @Override
    public void initialize(JsonValue data) {
        if (data != null) {
            boolean expanded = data.getBoolean(property, false);
            setExpanded(expanded);
        }
        child.initialize(data);
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(collapsibleContainer.isExpanded()));
        child.serializePart(object);
    }
}
