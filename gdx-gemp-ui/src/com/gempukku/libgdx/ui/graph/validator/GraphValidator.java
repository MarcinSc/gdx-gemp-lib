package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public interface GraphValidator {
    GraphValidationResult validateGraph(Graph graph, BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver);
}
