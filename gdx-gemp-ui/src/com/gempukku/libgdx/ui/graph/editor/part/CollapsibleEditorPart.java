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

public class CollapsibleEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final GraphNodeEditorPart child;
    private final CollapsibleContainer collapsibleContainer;

    public CollapsibleEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart) {
        this.property = property;
        this.child = graphNodeEditorPart;
        this.collapsibleContainer = new CollapsibleContainer(child.getActor());

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
        collapsibleContainer.setExpanded(expanded);
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
