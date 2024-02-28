package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.graph.data.*;

public class FieldTypeValidator implements GraphValidator {
    private BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver;

    public FieldTypeValidator(BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver) {
        this.nodeConfigurationResolver = nodeConfigurationResolver;
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();

        ObjectSet<String> validatedConnectionsForNode = new ObjectSet<>();
        ObjectMap<String, ObjectMap<String, Array<String>>> nodeInputsCache = new ObjectMap<>();

        for (GraphNode node : graph.getNodes()) {
            String startNode = node.getId();
            cacheNodeInputs(graph, nodeConfigurationResolver, nodeInputsCache, graph.getNodeById(startNode));
            validateConnectionsToNode(graph, result, validatedConnectionsForNode, nodeInputsCache, startNode);
        }

        return result;
    }

    @Override
    public GraphValidationResult validateSubGraph(Graph graph, String startNode) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();

        ObjectSet<String> validatedConnectionsForNode = new ObjectSet<>();
        ObjectMap<String, ObjectMap<String, Array<String>>> nodeInputsCache = new ObjectMap<>();

        cacheNodeInputs(graph, nodeConfigurationResolver, nodeInputsCache, graph.getNodeById(startNode));
        validateConnectionsToNode(graph, result, validatedConnectionsForNode, nodeInputsCache, startNode);

        return result;
    }

    private void validateConnectionsToNode(Graph graph, DefaultGraphValidationResult result, ObjectSet<String> validatedConnectionsForNode, ObjectMap<String, ObjectMap<String, Array<String>>> nodeInputsCache, String nodeId) {
        if (!validatedConnectionsForNode.contains(nodeId)) {
            Array<GraphConnection> connectionsToNode = getConnectionsTo(graph, nodeId);
            for (GraphConnection connection : connectionsToNode) {
                GraphNode nodeFrom = graph.getNodeById(connection.getNodeFrom());
                GraphNode nodeTo = graph.getNodeById(connection.getNodeTo());
                NodeConfiguration nodeFromConfiguration = nodeConfigurationResolver.evaluate(nodeFrom.getType(), nodeFrom.getData());
                NodeConfiguration nodeToConfiguration = nodeConfigurationResolver.evaluate(nodeTo.getType(), nodeTo.getData());
                if (nodeFromConfiguration != null && nodeToConfiguration != null) {
                    GraphNodeOutput output = nodeFromConfiguration.getNodeOutputs().get(connection.getFieldFrom());
                    GraphNodeInput input = nodeToConfiguration.getNodeInputs().get(connection.getFieldTo());

                    String fieldType = output.determineFieldType(nodeInputsCache.get(nodeFrom.getId()));
                    if (!input.acceptsFieldType(fieldType)) {
                        result.addErrorConnection(connection);
                    }
                }
            }
            validatedConnectionsForNode.add(nodeId);
            for (GraphConnection graphConnection : connectionsToNode) {
                String nodeFrom = graphConnection.getNodeFrom();
                validateConnectionsToNode(graph, result, validatedConnectionsForNode, nodeInputsCache, nodeFrom);
            }
        }
    }

    private void cacheNodeInputs(Graph graph, BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver,
                                 ObjectMap<String, ObjectMap<String, Array<String>>> inputsCache, GraphNode node) {
        ObjectMap<String, Array<String>> nodeInputs = inputsCache.get(node.getId());
        if (nodeInputs == null) {
            nodeInputs = new ObjectMap<>();
            inputsCache.put(node.getId(), nodeInputs);

            NodeConfiguration nodeConfiguration = nodeConfigurationResolver.evaluate(node.getType(), node.getData());
            if (nodeConfiguration != null) {
                // Doing weird stuff, to avoid errors in Gdx with iterators being called nested
                for (ObjectMap.Entry<String, ? extends GraphNodeInput> nodeInput : new ObjectMap.Entries<>(nodeConfiguration.getNodeInputs())) {
                    Array<GraphConnection> incomingConnections = getConnectionsTo(graph, node.getId(), nodeInput.value.getFieldId());

                    Array<String> types = new Array<>();
                    // Doing weird stuff, to avoid errors in Gdx with iterators being called nested
                    for (int i = 0; i < incomingConnections.size; i++) {
                        GraphConnection incomingConnection = incomingConnections.get(i);
                        GraphNode nodeFrom = graph.getNodeById(incomingConnection.getNodeFrom());
                        NodeConfiguration nodeFromConfiguration = nodeConfigurationResolver.evaluate(nodeFrom.getType(), nodeFrom.getData());
                        if (nodeFromConfiguration != null) {
                            GraphNodeOutput output = nodeFromConfiguration.getNodeOutputs().get(incomingConnection.getFieldFrom());
                            cacheNodeInputs(graph, nodeConfigurationResolver, inputsCache, nodeFrom);
                            String fieldType = output.determineFieldType(inputsCache.get(nodeFrom.getId()));
                            types.add(fieldType);
                        }
                    }
                    nodeInputs.put(nodeInput.key, types);
                }
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

    private Array<GraphConnection> getConnectionsTo(Graph graph, String nodeId, String fieldId) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId) && connection.getFieldTo().equals(fieldId))
                result.add(connection);
        }
        return result;
    }
}
