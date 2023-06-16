package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;

public class SumGraphValidator implements GraphValidator {
    private Array<GraphValidator> graphValidators = new Array<>();

    public SumGraphValidator(GraphValidator... validators) {
        graphValidators.addAll(validators);
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        SumGraphValidationResult result = new SumGraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateGraph(graph);
            result.addResult(validationResult);
        }
        return result;
    }

    @Override
    public GraphValidationResult validateSubGraph(Graph graph, String startNode) {
        SumGraphValidationResult result = new SumGraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateSubGraph(graph, startNode);
            result.addResult(validationResult);
        }
        return result;
    }
}
