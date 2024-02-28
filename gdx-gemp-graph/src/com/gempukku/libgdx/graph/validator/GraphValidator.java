package com.gempukku.libgdx.graph.validator;

import com.gempukku.libgdx.graph.data.Graph;

public interface GraphValidator {
    GraphValidationResult validateGraph(Graph graph);
    GraphValidationResult validateSubGraph(Graph graph, String startNode);
}
