package com.collibra.codingchallenge.graphs;

import com.collibra.codingchallenge.Messages;
import com.collibra.codingchallenge.commands.*;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.collibra.codingchallenge.Messages.*;
import static com.collibra.codingchallenge.commands.GraphCommand.match;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public final class GraphManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphManager.class);

    private final Graph<Node, Edge> graph = GraphOps.createGraph();

    public String handle(final GraphCommand command) {
        synchronized (graph) {
            LOGGER.info("Handling {}", command);
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
        return format(SHORTEST_PATH, weight.get());
    }

    private String handleCloserThan(final CloserThan command) {

        final Node start = new Node(command.node);

        if (!graph.containsVertex(start)) {
            return NODE_NOT_FOUND;
        }

        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);

        final Predicate<Node> keepNode =
                node -> {
                    if (node.equals(start)) {
                        return false;
                    }
                    final Number distance = shortestPath.getDistance(start, node);
                    if (distance == null) {
                        return false;
                    }
                    final int weight = distance.intValue();
                    return weight < command.weight;
                };

        final VertexPredicateFilter<Node, Edge> nodeFilter = new VertexPredicateFilter<>(keepNode);
        final Graph<Node, Edge> closerThan = nodeFilter.transform(graph);

        final List<String> names = closerThan.getVertices().stream().map(n -> n.name).collect(toList());

        return Messages.closerThan(names);
    }
}
