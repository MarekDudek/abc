package com.collibra.codingchallenge.graphs;

import com.collibra.codingchallenge.commands.*;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.collibra.codingchallenge.commands.GraphCommand.match;
import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public final class JungGraphManager implements GraphManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JungGraphManager.class);

    private final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();

    @Override
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
        final Node node = new Node(command.node);
        final boolean added = graph.addVertex(node);
        return added
                ? "NODE ADDED"
                : "ERROR: NODE ALREADY EXISTS";
    }

    private String handleAddEdge(final AddEdge command) {
        final Node from = new Node(command.from);
        final Node to = new Node(command.to);
        if (graph.containsVertex(from) && graph.containsVertex(to)) {
            final Edge e = new Edge(command.weight, command.from, command.to);
            if (graph.containsEdge(e)) {
                LOGGER.warn("{} already exists", e);
            } else {
                graph.addEdge(e, from, to, DIRECTED);
            }
            return "EDGE ADDED";
        } else {
            return "ERROR: NODE NOT FOUND";
        }
    }

    private String handleRemoveNode(final RemoveNode command) {
        final Node node = new Node(command.node);
        final boolean removed = graph.removeVertex(node);
        return removed
                ? "NODE REMOVED"
                : "ERROR: NODE NOT FOUND";
    }

    private String handleRemoveEdge(final RemoveEdge command) {
        final Node f = new Node(command.from);
        final Node t = new Node(command.to);
        if (graph.containsVertex(f) && graph.containsVertex(t)) {
            final List<Edge> edges = graph.getEdges().stream().
                    filter(
                            e -> e.from.equals(command.from) && e.to.equals(command.to)
                    ).collect(toList());
            for (final Edge e : edges) {
                if (graph.containsEdge(e)) {
                    graph.removeEdge(e);
                }
            }
            return "EDGE REMOVED";
        } else {
            return "ERROR: NODE NOT FOUND";
        }
    }

    private String handleShortestPath(final ShortestPath command) {
        final Node f = new Node(command.from);
        final Node t = new Node(command.to);
        final DijkstraShortestPath<Node, Edge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        try {
            final Number distance = algorithm.getDistance(f, t);
            final int weight = distance == null
                    ? Integer.MAX_VALUE
                    : distance.intValue();
            return format("%d", weight);
        } catch (final IllegalArgumentException e) {
            return "ERROR: NODE NOT FOUND";
        }
    }

    private String handleCloserThan(final CloserThan command) {
        return null;
    }
}