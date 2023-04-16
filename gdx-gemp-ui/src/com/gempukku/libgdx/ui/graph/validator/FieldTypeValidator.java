package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.BiFunction;
import com.gempukku.libgdx.ui.graph.data.*;

public class FieldTypeValidator implements GraphValidator {
    private BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver;

    public FieldTypeValidator(BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver) {
        this.nodeConfigurationResolver = nodeConfigurationResolver;
    }

    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        GraphValidationResult result = new GraphValidationResult();

        ObjectMap<String, ObjectMap<String, Array<String>>> nodeInputsCache = new ObjectMap<>();

        for (GraphConnection connection : graph.getConnections()) {
            GraphNode nodeFrom = graph.getNodeById(connection.getNodeFrom());
            NodeConfiguration nodeFromConfiguration = nodeConfigurationResolver.evaluate(nodeFrom.getType(), nodeFrom.getData());
            GraphNodeOutput output = nodeFromConfiguration.getNodeOutputs().get(connection.getFieldFrom());
            GraphNode nodeTo = graph.getNodeById(connection.getNodeTo());
            NodeConfiguration nodeToConfiguration = nodeConfigurationResolver.evaluate(nodeTo.getType(), nodeTo.getData());
            GraphNodeInput input = nodeToConfiguration.getNodeInputs().get(connection.getFieldTo());

            String fieldType = output.determineFieldType(getNodeInputs(graph, nodeConfigurationResolver, nodeInputsCache, nodeFrom));
            if (!input.getAcceptedPropertyTypes().contains(fieldType, false)) {
                result.addErrorConnection(connection);
            }
        }

        return result;
    }

    private ObjectMap<String, Array<String>> getNodeInputs(Graph graph, BiFunction<String, JsonValue, NodeConfiguration> nodeConfigurationResolver,
                                                           ObjectMap<String, ObjectMap<String, Array<String>>> inputsCache, GraphNode node) {
        ObjectMap<String, Array<String>> nodeInputs = inputsCache.get(node.getId());
        if (nodeInputs == null) {
            nodeInputs = new ObjectMap<>();
            inputsCache.put(node.getId(), nodeInputs);

            NodeConfiguration nodeConfiguration = nodeConfigurationResolver.evaluate(node.getType(), node.getData());

            for (ObjectMap.Entry<String, GraphNodeInput> nodeInput : nodeConfiguration.getNodeInputs()) {
                Array<GraphConnection> incomingConnections = getConnectionsTo(graph, node.getId(), nodeInput.value.getFieldId());

                Array<String> types = new Array<>();
                for (GraphConnection incomingConnection : incomingConnections) {
                    GraphNode nodeFrom = graph.getNodeById(incomingConnection.getNodeFrom());
                    NodeConfiguration nodeFromConfiguration = nodeConfigurationResolver.evaluate(nodeFrom.getType(), nodeFrom.getData());
                    GraphNodeOutput output = nodeFromConfiguration.getNodeOutputs().get(incomingConnection.getFieldFrom());
                    String fieldType = output.determineFieldType(getNodeInputs(graph, nodeConfigurationResolver, inputsCache, nodeFrom));
                    types.add(fieldType);
                }
                nodeInputs.put(nodeInput.key, types);
            }
        }
        return nodeInputs;
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
