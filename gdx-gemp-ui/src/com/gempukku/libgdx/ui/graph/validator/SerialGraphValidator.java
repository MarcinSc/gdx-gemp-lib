package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class SerialGraphValidator implements GraphValidator {
    private Array<GraphValidator> graphValidators = new Array<>();

    public SerialGraphValidator(GraphValidator...validators) {
        graphValidators.addAll(validators);
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph, String startNode) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateGraph(graph, startNode);
            if (validationResult.hasErrors()) {
                moveWarnings(validationResult, result);
                moveErrors(validationResult, result);
                return result;
            } else {
                moveWarnings(validationResult, result);
            }
        }
        return result;
    }

    private static void moveWarnings(GraphValidationResult from, DefaultGraphValidationResult to) {
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

    private static void moveErrors(GraphValidationResult from, DefaultGraphValidationResult to) {
        for (String errorNode : from.getErrorNodes()) {
            to.addErrorNode(errorNode);
        }
        for (GraphConnection errorConnections : from.getErrorConnections()) {
            to.addErrorConnection(errorConnections);
        }
        for (NodeConnector errorConnector : from.getErrorConnectors()) {
            to.addErrorConnector(errorConnector);
        }
    }
}
