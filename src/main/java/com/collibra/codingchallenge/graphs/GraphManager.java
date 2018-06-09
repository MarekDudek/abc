package com.collibra.codingchallenge.graphs;

import com.collibra.codingchallenge.commands.*;
import com.hendrix.erdos.graphs.IDirectedGraph;
import com.hendrix.erdos.types.IVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.collibra.codingchallenge.commands.GraphCommand.match;
import static com.collibra.codingchallenge.graphs.ErdosGraphs.*;
import static java.lang.String.format;

public final class GraphManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphManager.class);

    private final IDirectedGraph graph = directedGraph();


    public String handle(final GraphCommand command) {
        synchronized (graph) {
            LOGGER.info("handling {}", command);
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
    }


    private String handleAddNode(final AddNode command) {
        final IVertex<String> node = node(command.node);
        final boolean added = addNode(graph, node);
        return added
                ? "NODE ADDED"
                : "ERROR: NODE ALREADY EXISTS";
    }

    private String handleAddEdge(final AddEdge command) {
        final IVertex<String> from = node(command.from);
        final IVertex<String> to = node(command.to);
        final int weight = command.weight;
        final boolean added = addEdge(graph, from, to, weight);
        return added
                ? "EDGE ADDED"
                : "ERROR: NODE NOT FOUND";
    }

    private String handleRemoveNode(final RemoveNode command) {
        final IVertex<String> node = node(command.node);
        final boolean removed = removeNode(graph, node);
        return removed
                ? "NODE REMOVED"
                : "ERROR: NODE NOT FOUND";
    }

    private String handleRemoveEdge(final RemoveEdge command) {
        final IVertex<String> from = node(command.from);
        final IVertex<String> to = node(command.to);
        final int removed = removeEdges(graph, from, to);
        return removed > 0
                ? "EDGE REMOVED"
                : "ERROR: NODE NOT FOUND";
    }

    private String handleShortestPath(final ShortestPath command) {
        final IVertex<String> from = node(command.from);
        final IVertex<String> to = node(command.to);
        final Optional<Integer> weight = shortestPath(graph, from, to);
        return weight.map(
                w -> format("%d", w)
        ).orElse(
                "ERROR: NODE NOT FOUND"
        );
    }

    private String handleCloserThan(final CloserThan command) {
        return null;
    }
}
