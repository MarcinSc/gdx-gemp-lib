package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;

public class SerialGraphValidator implements GraphValidator {
    private Array<GraphValidator> graphValidators = new Array<>();

    public SerialGraphValidator(GraphValidator...validators) {
        graphValidators.addAll(validators);
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        GraphValidationResult result = new GraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateGraph(graph);
            if (validationResult.hasErrors()) {
                // If it has error, stop validating, and just move existing warnings to this one to return
                moveWarnings(result, validationResult);
                return validationResult;
            } else {
                moveWarnings(validationResult, result);
            }
        }
        return result;
    }

    private static void moveWarnings(GraphValidationResult from, GraphValidationResult to) {
        for (String warningNode : from.getWarningNodes()) {
            to.addWarningNode(warningNode);
        }
        for (GraphConnection warningConnection : from.getWarningConnections()) {
            to.addWarningConnection(warningConnection);
        }
        for (NodeConnector warningConnector : from.getWarningConnectors()) {
            to.addWarningConnector(warningConnector);
        }
    }
}
