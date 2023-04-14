package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInput;

public class RequiredInputsValidator implements GraphValidator {
    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        GraphValidationResult result = new GraphValidationResult();

        for (GraphNode node : graph.getNodes()) {
            for (ObjectMap.Entry<String, GraphNodeInput> entry : node.getConfiguration().getNodeInputs().entries()) {
                if (entry.value.isRequired() && !hasConnectionToInput(graph, node.getId(), entry.key))
                    result.addErrorConnector(new NodeConnector(node.getId(), entry.key));
            }
        }

        return result;
    }

    private boolean hasConnectionToInput(Graph graph, String nodeId, String fieldId) {
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId) && connection.getFieldTo().equals(fieldId))
                return true;
        }
        return false;
    }
}
