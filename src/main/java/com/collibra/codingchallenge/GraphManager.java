package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.*;
import com.collibra.codingchallenge.graphs.Edge;
import com.collibra.codingchallenge.graphs.GraphOps;
import com.collibra.codingchallenge.graphs.Node;
import com.collibra.codingchallenge.parsing.Messages;
import edu.uci.ics.jung.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.collibra.codingchallenge.commands.GraphCommand.match;
import static com.collibra.codingchallenge.parsing.Messages.*;

final class GraphManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphManager.class);

    private final Graph<Node, Edge> graph = GraphOps.createGraph();

    String handle(final GraphCommand command) {
        synchronized (graph) {
            LOGGER.debug("Handling {}", command);
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
        final boolean added = GraphOps.addNode(graph, command.node);
        if (!added) {
            return NODE_ALREADY_EXISTS;
        }
        return NODE_ADDED;
    }

    private String handleAddEdge(final AddEdge command) {
        final boolean added = GraphOps.addEdge(graph, command.from, command.to, command.weight);
        if (!added) {
            return NODE_NOT_FOUND;
        }
        return EDGE_ADDED;
    }

    private String handleRemoveNode(final RemoveNode command) {
        final boolean removed = GraphOps.removeNode(graph, command.node);
        if (!removed) {
            return NODE_NOT_FOUND;
        }
        return NODE_REMOVED;
    }

    private String handleRemoveEdge(final RemoveEdge command) {
        final boolean removed = GraphOps.removeEdge(graph, command.from, command.to);
        if (!removed) {
            return NODE_NOT_FOUND;
        }
        return EDGE_REMOVED;

    }

    private String handleShortestPath(final ShortestPath command) {
        final Optional<Integer> weight = GraphOps.shortestPath(graph, command.from, command.to);
        if (!weight.isPresent()) {
            return NODE_NOT_FOUND;
        }
        return Messages.shortestPath(weight.get());
    }

    private String handleCloserThan(final CloserThan command) {
        final Optional<List<String>> nodes = GraphOps.closerThan(graph, command.weight, command.node);
        if (!nodes.isPresent()) {
            return NODE_NOT_FOUND;
        }
        return Messages.closerThan(nodes.get());
    }
}
