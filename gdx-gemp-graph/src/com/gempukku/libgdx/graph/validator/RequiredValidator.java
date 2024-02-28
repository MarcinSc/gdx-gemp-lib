package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.data.*;

public class RequiredValidator implements GraphValidator {
    private BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver;
    private boolean ignoreMissingProducers;

    public RequiredValidator(BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver) {
        this.nodeConfigurationResolver = nodeConfigurationResolver;
    }

    public void setIgnoreMissingProducers(boolean ignoreMissingProducers) {
        this.ignoreMissingProducers = ignoreMissingProducers;
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
            if (nodeConfiguration != null) {
                for (ObjectMap.Entry<String, ? extends GraphNodeInput> entry : nodeConfiguration.getNodeInputs().entries()) {
                    if (entry.value.isRequired() && !hasConnectionToInput(graph, node.getId(), entry.key))
                        result.addErrorConnector(new NodeConnector(node.getId(), entry.key));
                }
                for (ObjectMap.Entry<String, ? extends GraphNodeOutput> entry : nodeConfiguration.getNodeOutputs().entries()) {
                    if (entry.value.isRequired() && !hasConnectionToOutput(graph, node.getId(), entry.key))
                        result.addErrorConnector(new NodeConnector(node.getId(), entry.key));
                }
            } else if (!ignoreMissingProducers) {
                result.addErrorNode(nodeId);
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

    private boolean hasConnectionToOutput(Graph graph, String nodeId, String fieldId) {
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeFrom().equals(nodeId) && connection.getFieldFrom().equals(fieldId))
                return true;
        }
        return false;
    }
}
