package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.ui.graph.data.Graph;

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
                for (String warningNode : result.getWarningNodes()) {
                    validationResult.addWarningNode(warningNode);
                }
                return validationResult;
            } else {
                for (String warningNode : validationResult.getWarningNodes()) {
                    result.addWarningNode(warningNode);
                }
            }
        }
        return result;
    }
}
