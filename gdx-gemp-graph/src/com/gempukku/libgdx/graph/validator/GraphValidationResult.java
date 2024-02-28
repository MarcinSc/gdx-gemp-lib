package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.GraphConnection;

public interface GraphValidationResult {
    ObjectSet<String> getErrorNodes();

    ObjectSet<String> getWarningNodes();

    ObjectSet<GraphConnection> getErrorConnections();

    ObjectSet<GraphConnection> getWarningConnections();

    ObjectSet<NodeConnector> getErrorConnectors();

    ObjectSet<NodeConnector> getWarningConnectors();

    boolean hasErrors();

    boolean hasWarnings();
}
