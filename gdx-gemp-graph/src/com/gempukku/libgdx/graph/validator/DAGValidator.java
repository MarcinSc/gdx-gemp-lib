package com.gempukku.libgdx.graph.validator;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;

// Checks that the graph is acyclic
public class DAGValidator implements GraphValidator {
    @Override
    public GraphValidationResult validateGraph(Graph graph) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();
        checkCyclic(result, graph);
        return result;
    }

    @Override
    public GraphValidationResult validateSubGraph(Graph graph, String startNode) {
        DefaultGraphValidationResult result = new DefaultGraphValidationResult();
        checkCyclic(result, graph, startNode);
        return result;
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private boolean isCyclicUtil(DefaultGraphValidationResult validationResult, Graph graph, String nodeId, ObjectSet<String> visited,
                                 ObjectSet<String> recStack) {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack.contains(nodeId)) {
            validationResult.addErrorNode(nodeId);
            return true;
        }

        if (visited.contains(nodeId))
            return false;

        visited.add(nodeId);
        recStack.add(nodeId);

        ObjectSet<String> connectedNodes = new ObjectSet<>();
        for (GraphConnection incomingConnection : getIncomingConnections(graph, nodeId)) {
            connectedNodes.add(incomingConnection.getNodeFrom());
        }

        for (String connectedNode : connectedNodes) {
            if (isCyclicUtil(validationResult, graph, connectedNode, visited, recStack)) {
                return true;
            }
        }
        recStack.remove(nodeId);

        return false;
    }

    private void checkCyclic(DefaultGraphValidationResult validationResult, Graph graph) {
        ObjectSet<String> visited = new ObjectSet<>();
        ObjectSet<String> recStack = new ObjectSet<>();

        for (GraphNode node : graph.getNodes()) {
            // Call the recursive helper function to
            // detect cycle in different DFS trees
            if (isCyclicUtil(validationResult, graph, node.getId(), visited, recStack)) {
                return;
            }
        }

        for (GraphNode node : graph.getNodes()) {
            String nodeId = node.getId();
            if (!visited.contains(nodeId)) {
                validationResult.addWarningNode(node.getId());
            }
        }
    }

    private void checkCyclic(DefaultGraphValidationResult validationResult, Graph graph, String start) {
        ObjectSet<String> visited = new ObjectSet<>();
        ObjectSet<String> recStack = new ObjectSet<>();

        // Call the recursive helper function to
        // detect cycle in different DFS trees
        if (isCyclicUtil(validationResult, graph, start, visited, recStack)) {
            return;
        }

        for (GraphNode node : graph.getNodes()) {
            String nodeId = node.getId();
            if (!visited.contains(nodeId)) {
                validationResult.addWarningNode(node.getId());
            }
        }
    }

    private Iterable<GraphConnection> getIncomingConnections(Graph graph, String nodeId) {
        Array<GraphConnection> result = new Array<>();
        for (GraphConnection connection : graph.getConnections()) {
            if (connection.getNodeTo().equals(nodeId))
                result.add(connection);
        }
        return result;
    }
}
