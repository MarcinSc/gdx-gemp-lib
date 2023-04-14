package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditor;
import com.kotcrab.vis.ui.widget.VisWindow;

public class GraphNodeWindow extends VisWindow implements GraphNode, Disposable {
    private String nodeId;
    private GraphNodeEditor graphNodeEditor;
    private ObjectSet<String> connectorsWithError = new ObjectSet<>();
    private Vector2 position;

    public GraphNodeWindow(String nodeId, GraphNodeEditor graphNodeEditor, String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        this.nodeId = nodeId;
        this.graphNodeEditor = graphNodeEditor;
        add(graphNodeEditor.getActor()).grow().row();
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
        if (position != null) {
            positionChanged(position.x, position.y, getX(), getY());
        } else {
            position = new Vector2();
        }
        position.set(getX(), getY());
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
    public JsonValue getData() {
        return graphNodeEditor.getData();
    }

    @Override
    public NodeConfiguration getConfiguration() {
        return graphNodeEditor.getConfiguration();
    }

    @Override
    public void dispose() {
        graphNodeEditor.dispose();
    }
}
