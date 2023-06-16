package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.NodeConnector;
import com.gempukku.libgdx.ui.graph.data.*;

public class MultipleConnectionsValidator implements GraphValidator {
    private BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver;

    public MultipleConnectionsValidator(BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver) {
        this.nodeConfigurationResolver = nodeConfigurationResolver;
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();
        ObjectSet<String> validatedNodes = new ObjectSet<>();

        for (GraphNode node : graph.getNodes()) {
            validateConnectorsForNode(graph, node.getId(), result, validatedNodes);
        }

        return result;
    }

    @Override
    public GraphValidationResult validateSubGraph(Graph graph, String startNode) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();
        ObjectSet<String> validatedNodes = new ObjectSet<>();

        validateConnectorsForNode(graph, startNode, result, validatedNodes);

        return result;
    }

    private void validateConnectorsForNode(Graph graph, String nodeId, DefaultGraphValidationResult result, ObjectSet<String> validatedNodes) {
        if (!validatedNodes.contains(nodeId)) {
            GraphNode node = graph.getNodeById(nodeId);
            NodeConfiguration nodeConfiguration = nodeConfigurationResolver.evaluate(node.getType(), node.getData());
            for (ObjectMap.Entry<String, ? extends GraphNodeInput> entry : nodeConfiguration.getNodeInputs().entries()) {
                if (!entry.value.acceptsMultipleConnections() && hasMoreThanOneConnectionToInput(graph, node.getId(), entry.key))
                    result.addErrorConnector(new NodeConnector(node.getId(), entry.key));
            }
            for (ObjectMap.Entry<String, ? extends GraphNodeOutput> entry : nodeConfiguration.getNodeOutputs().entries()) {
                if (!entry.value.acceptsMultipleConnections() && hasMoreThanOneConnectionFromOutput(graph, node.getId(), entry.key))
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

    private boolean hasMoreThanOneConnectionFromOutput(Graph graph, String nodeId, String fieldId) {
        boolean found = false;
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeFrom().equals(nodeId) && connection.getFieldFrom().equals(fieldId)) {
                if (found)
                    return true;
                else
                    found = true;
            }
        }
        return false;
    }

    private boolean hasMoreThanOneConnectionToInput(Graph graph, String nodeId, String fieldId) {
        boolean found = false;
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId) && connection.getFieldTo().equals(fieldId)) {
                if (found)
                    return true;
                else
                    found = true;
            }
        }
        return false;
    }
}
