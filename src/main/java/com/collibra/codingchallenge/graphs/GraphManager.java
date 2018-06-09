package com.collibra.codingchallenge.graphs;

import com.collibra.codingchallenge.GraphCommand;
import com.collibra.codingchallenge.commands.*;
import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.types.IVertex;

import static com.collibra.codingchallenge.commands.GraphCommands.match;
import static com.collibra.codingchallenge.graphs.Graphs.*;

public final class GraphManager {

    private final AbstractGraph graph = directedGraph();

    public String handle(final GraphCommand command) {
        return match(
                command,
                this::handleAddNode,
                this::handleAddEdge,
                this::handleRemoveNode,
                this::handleRemoveEdge,
                this::handleShortestPath,
                this::handleCloserThan
        );
    }

    
    private String handleAddNode(final AddNode command) {
        final IVertex<String> node = node(command.node);
        final boolean added = addNode(graph, node);
        return added ? "NODE ADDED" : "ERROR: NODE ALREADY EXISTS";
    }

    private String handleAddEdge(final AddEdge command) {
        final IVertex<String> from = node(command.from);
        final IVertex<String> to = node(command.to);
        final int weight = command.weight;
        final boolean added = addEdge(graph, from, to, weight);
        return added ? "EDGE ADDED" : "ERROR: NODE NOT FOUND";
    }

    private String handleRemoveNode(final RemoveNode command) {
        final IVertex<String> node = node(command.node);
        final boolean removed = removeNode(graph, node);
        return removed ? "NODE REMOVED" : "ERROR: NODE NOT FOUND";
    }

    private String handleRemoveEdge(final RemoveEdge command) {
        final IVertex<String> from = node(command.from);
        final IVertex<String> to = node(command.to);
        final int removed = removeEdges(graph, from, to);
        return removed > 0 ? "EDGE REMOVED" : "ERROR: NODE NOT FOUND";
    }

    private String handleShortestPath(final ShortestPath command) {
        return null;
    }

    private String handleCloserThan(final CloserThan command) {
        return null;
    }
}
