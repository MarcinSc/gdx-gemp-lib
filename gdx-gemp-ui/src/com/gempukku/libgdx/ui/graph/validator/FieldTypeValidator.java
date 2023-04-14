package com.gempukku.libgdx.ui.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.ui.graph.data.*;

public class FieldTypeValidator implements GraphValidator {
    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        GraphValidationResult result = new GraphValidationResult();

        ObjectMap<String, ObjectMap<String, Array<String>>> nodeInputsCache = new ObjectMap<>();

        for (GraphConnection connection : graph.getConnections()) {
            GraphNode nodeFrom = graph.getNodeById(connection.getNodeFrom());
            GraphNodeOutput output = nodeFrom.getConfiguration().getNodeOutputs().get(connection.getFieldFrom());
            GraphNode nodeTo = graph.getNodeById(connection.getNodeTo());
            GraphNodeInput input = nodeTo.getConfiguration().getNodeInputs().get(connection.getFieldTo());

            String fieldType = output.determineFieldType(getNodeInputs(graph, nodeInputsCache, nodeFrom));
            if (!input.getAcceptedPropertyTypes().contains(fieldType, false)) {
                result.addErrorConnection(connection);
            }
        }

        return result;
    }

    private ObjectMap<String, Array<String>> getNodeInputs(Graph graph, ObjectMap<String, ObjectMap<String, Array<String>>> inputsCache, GraphNode node) {
        ObjectMap<String, Array<String>> nodeInputs = inputsCache.get(node.getId());
        if (nodeInputs == null) {
            nodeInputs = new ObjectMap<>();
            inputsCache.put(node.getId(), nodeInputs);

            for (ObjectMap.Entry<String, GraphNodeInput> nodeInput : node.getConfiguration().getNodeInputs()) {
                Array<GraphConnection> incomingConnections = getConnectionsTo(graph, node.getId(), nodeInput.value.getFieldId());

                Array<String> types = new Array<>();
                for (GraphConnection incomingConnection : incomingConnections) {
                    GraphNode nodeFrom = graph.getNodeById(incomingConnection.getNodeFrom());
                    GraphNodeOutput output = nodeFrom.getConfiguration().getNodeOutputs().get(incomingConnection.getFieldFrom());
                    String fieldType = output.determineFieldType(getNodeInputs(graph, inputsCache, nodeFrom));
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
