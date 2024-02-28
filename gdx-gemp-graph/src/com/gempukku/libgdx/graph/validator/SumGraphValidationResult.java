package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.GraphConnection;

public class SumGraphValidationResult implements GraphValidationResult {
    private final ObjectSet<String> errorNodes = new ObjectSet<>();
    private final ObjectSet<String> warningNodes = new ObjectSet<>();
    private final ObjectSet<GraphConnection> errorConnections = new ObjectSet<>();
    private final ObjectSet<GraphConnection> warningConnections = new ObjectSet<>();
    private final ObjectSet<NodeConnector> errorConnectors = new ObjectSet<>();
    private final ObjectSet<NodeConnector> warningConnectors = new ObjectSet<>();

    public void addResult(GraphValidationResult result) {
        copyWarnings(result);
        copyErrors(result);
    }

    private void copyWarnings(GraphValidationResult from) {
        warningNodes.addAll(from.getWarningNodes());
        warningConnections.addAll(from.getWarningConnections());
        warningConnectors.addAll(from.getWarningConnectors());
    }

    private void copyErrors(GraphValidationResult from) {
        errorNodes.addAll(from.getErrorNodes());
        errorConnections.addAll(from.getErrorConnections());
        errorConnectors.addAll(from.getErrorConnectors());
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
        return !errorConnections.isEmpty() || !errorConnectors.isEmpty() || !errorNodes.isEmpty();
    }

    @Override
    public boolean hasWarnings() {
        return !warningConnections.isEmpty() || !warningConnectors.isEmpty() || !warningNodes.isEmpty();
    }
}
