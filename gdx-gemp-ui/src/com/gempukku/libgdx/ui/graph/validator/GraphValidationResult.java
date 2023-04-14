package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.NodeConnector;

public class GraphValidationResult {
    private final ObjectSet<String> errorNodes = new ObjectSet<>();
    private final ObjectSet<String> warningNodes = new ObjectSet<>();
    private final ObjectSet<GraphConnection> errorConnections = new ObjectSet<>();
    private final ObjectSet<GraphConnection> warningConnections = new ObjectSet<>();
    private final ObjectSet<NodeConnector> errorConnectors = new ObjectSet<>();
    private final ObjectSet<NodeConnector> warningConnectors = new ObjectSet<>();

    public void addErrorNode(String nodeId) {
        errorNodes.add(nodeId);
    }

    public void addWarningNode(String nodeId) {
        warningNodes.add(nodeId);
    }

    public void addErrorConnection(GraphConnection connection) {
        errorConnections.add(connection);
    }

    public void addWarningConnection(GraphConnection connection) {
        warningConnections.add(connection);
    }

    public void addErrorConnector(NodeConnector nodeConnector) {
        errorConnectors.add(nodeConnector);
    }

    public void addWarningConnector(NodeConnector nodeConnector) {
        warningConnectors.add(nodeConnector);
    }

    public ObjectSet<String> getErrorNodes() {
        return errorNodes;
    }

    public ObjectSet<String> getWarningNodes() {
        return warningNodes;
    }

    public ObjectSet<GraphConnection> getErrorConnections() {
        return errorConnections;
    }

    public ObjectSet<GraphConnection> getWarningConnections() {
        return warningConnections;
    }

    public ObjectSet<NodeConnector> getErrorConnectors() {
        return errorConnectors;
    }

    public ObjectSet<NodeConnector> getWarningConnectors() {
        return warningConnectors;
    }

    public boolean hasErrors() {
        return !errorNodes.isEmpty() || !errorConnections.isEmpty() || !errorConnectors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warningNodes.isEmpty() || !warningConnections.isEmpty() || !warningConnectors.isEmpty();
    }
}
