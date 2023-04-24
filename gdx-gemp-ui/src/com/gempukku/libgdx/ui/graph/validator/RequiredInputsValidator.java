package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.*;

import java.util.HashSet;

public class RequiredInputsValidator implements GraphValidator {
    private BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver;

    public RequiredInputsValidator(BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver) {
        this.nodeConfigurationResolver = nodeConfigurationResolver;
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph, String startNode) {
        GraphValidationResult result = new GraphValidationResult();
        ObjectSet<String> validatedNodes = new ObjectSet<>();

        validateConnectorsForNode(graph, startNode, result, validatedNodes);

        return result;
    }

    private void validateConnectorsForNode(Graph graph, String nodeId, GraphValidationResult result, ObjectSet<String> validatedNodes) {
        if (!validatedNodes.contains(nodeId)) {
            GraphNode node = graph.getNodeById(nodeId);
            NodeConfiguration nodeConfiguration = nodeConfigurationResolver.evaluate(node.getType(), node.getData());
            for (ObjectMap.Entry<String, GraphNodeInput> entry : nodeConfiguration.getNodeInputs().entries()) {
                if (entry.value.isRequired() && !hasConnectionToInput(graph, node.getId(), entry.key))
                    result.addErrorConnector(new NodeConnector(node.getId(), entry.key));
            }
            validatedNodes.add(nodeId);
            for (GraphConnection graphConnection : getConnectionsTo(graph, nodeId)) {
                String nodeFrom = graphConnection.getNodeFrom();
                validateConnectorsForNode(graph, nodeFrom, result, validatedNodes);
            }
        }
    }

    private Array<GraphConnection> getConnectionsTo(Graph graph, String nodeId) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId))
                result.add(connection);
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
