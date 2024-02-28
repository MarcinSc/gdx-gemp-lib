package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.data.Graph;

public class SerialGraphValidator implements GraphValidator {
    private Array<GraphValidator> graphValidators = new Array<>();

    public SerialGraphValidator(GraphValidator...validators) {
        graphValidators.addAll(validators);
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        SumGraphValidationResult result = new SumGraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateGraph(graph);
            result.addResult(validationResult);
            if (validationResult.hasErrors()) {
                return result;
            }
        }
        return result;
    }

    @Override
    public GraphValidationResult validateSubGraph(Graph graph, String startNode) {
        SumGraphValidationResult result = new SumGraphValidationResult();
        for (GraphValidator graphValidator : graphValidators) {
            GraphValidationResult validationResult = graphValidator.validateSubGraph(graph, startNode);
            result.addResult(validationResult);
            if (validationResult.hasErrors()) {
                return result;
            }
        }
        return result;
    }
}
