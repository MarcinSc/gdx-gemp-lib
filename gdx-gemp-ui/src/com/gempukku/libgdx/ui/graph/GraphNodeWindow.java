package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditor;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisWindow;

public class GraphNodeWindow extends VisWindow implements GraphNode, Disposable {
    private final String nodeId;
    private final GraphNodeEditor graphNodeEditor;
    private VisImageButton.VisImageButtonStyle closeButtonStyle;
    private final ObjectSet<String> connectorsWithError = new ObjectSet<>();
    private Vector2 lastPosition;

    public GraphNodeWindow(String nodeId, GraphNodeEditor graphNodeEditor, String title, WindowStyle windowStyle, VisImageButton.VisImageButtonStyle closeButtonStyle) {
        super(title, windowStyle);
        this.nodeId = nodeId;
        this.graphNodeEditor = graphNodeEditor;
        this.closeButtonStyle = closeButtonStyle;
        add(graphNodeEditor.getActor()).grow().row();
    }

    public void addCloseButton () {
        Label titleLabel = getTitleLabel();
        Table titleTable = getTitleTable();

        VisImageButton closeButton = new VisImageButton(closeButtonStyle);
        titleTable.add(closeButton).padRight(-getPadRight() + 0.7f);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                close();
            }
        });
        closeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return true;
            }
        });

        if (titleLabel.getLabelAlign() == Align.center && titleTable.getChildren().size == 2)
            titleTable.getCell(titleLabel).padLeft(closeButton.getWidth() * 2);
    }

    public void clearConnectorErrors() {
        connectorsWithError.clear();
    }

    public void setConnectorError(String fieldId) {
        connectorsWithError.add(fieldId);
    }

    public boolean isConnectorError(String fieldId) {
        return connectorsWithError.contains(fieldId);
    }

    @Override
    protected final void positionChanged() {
        if (lastPosition != null) {
            positionChanged(lastPosition.x, lastPosition.y, getX(), getY());
        } else {
            lastPosition = new Vector2();
        }
        lastPosition.set(getX(), getY());
    }

    protected void positionChanged(float fromX, float fromY, float toX, float toY) {

    }

    public GraphNodeEditor getGraphNodeEditor() {
        return graphNodeEditor;
    }

    @Override
    public String getId() {
        return nodeId;
    }

    @Override
    public String getType() {
        return graphNodeEditor.getConfiguration().getType();
    }

    @Override
    public JsonValue getData() {
        return graphNodeEditor.getData();
    }

    @Override
    public void dispose() {
        if (graphNodeEditor instanceof Disposable)
            ((Disposable) graphNodeEditor).dispose();
    }
}
