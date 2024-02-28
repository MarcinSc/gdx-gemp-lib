package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.GraphConnection;

public class DefaultGraphValidationResult implements GraphValidationResult {
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

    @Override
    public ObjectSet<String> getErrorNodes() {
        return errorNodes;
    }

    @Override
    public ObjectSet<String> getWarningNodes() {
        return warningNodes;
    }

    @Override
    public ObjectSet<GraphConnection> getErrorConnections() {
        return errorConnections;
    }

    @Override
    public ObjectSet<GraphConnection> getWarningConnections() {
        return warningConnections;
    }

    @Override
    public ObjectSet<NodeConnector> getErrorConnectors() {
        return errorConnectors;
    }

    @Override
    public ObjectSet<NodeConnector> getWarningConnectors() {
        return warningConnectors;
    }

    @Override
    public boolean hasErrors() {
        return !errorNodes.isEmpty() || !errorConnections.isEmpty() || !errorConnectors.isEmpty();
    }

    @Override
    public boolean hasWarnings() {
        return !warningNodes.isEmpty() || !warningConnections.isEmpty() || !warningConnectors.isEmpty();
    }
}
