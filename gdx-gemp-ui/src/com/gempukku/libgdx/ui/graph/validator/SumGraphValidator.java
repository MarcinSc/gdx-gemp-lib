package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class SumGraphValidator implements GraphValidator {
    private Array<GraphValidator> graphValidators = new Array<>();

    public SumGraphValidator(GraphValidator... validators) {
        graphValidators.addAll(validators);
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph, String startNode) {
        GraphValidationResult result = new GraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateGraph(graph, startNode);
            for (String errorNode : validationResult.getErrorNodes()) {
                result.addErrorNode(errorNode);
            }
            for (String warningNode : validationResult.getWarningNodes()) {
                result.addWarningNode(warningNode);
            }
            ObjectSet<GraphConnection> errorConnections = validationResult.getErrorConnections();
            for (GraphConnection errorConnection : errorConnections) {
                result.addErrorConnection(errorConnection);
            }
            for (NodeConnector errorConnector : validationResult.getErrorConnectors()) {
                result.addErrorConnector(errorConnector);
            }
        }
        return result;
    }
}
