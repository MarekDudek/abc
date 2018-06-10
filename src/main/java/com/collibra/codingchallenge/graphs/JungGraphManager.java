package com.collibra.codingchallenge.graphs;

import com.collibra.codingchallenge.Messages;
import com.collibra.codingchallenge.commands.*;
import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.collibra.codingchallenge.Messages.*;
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
        final Node node = new Node(command.node);
        final boolean added = graph.addVertex(node);
        return added ? NODE_ADDED : NODE_ALREADY_EXISTS;
    }

    private String handleAddEdge(final AddEdge command) {
        final Node from = new Node(command.from);
        final Node to = new Node(command.to);
        if (graph.containsVertex(from) && graph.containsVertex(to)) {
            final Edge edge = new Edge(command.weight, command.from, command.to);
            if (graph.containsEdge(edge)) {
                LOGGER.warn("Edge already exists {}", edge);
            } else {
                graph.addEdge(edge, from, to, DIRECTED);
            }
            return EDGE_ADDED;
        } else {
            return NODE_NOT_FOUND;
        }
    }

    private String handleRemoveNode(final RemoveNode command) {
        final Node node = new Node(command.node);
        final boolean removed = graph.removeVertex(node);
        return removed ? NODE_REMOVED : NODE_NOT_FOUND;
    }

    private String handleRemoveEdge(final RemoveEdge command) {
        final Node from = new Node(command.from);
        final Node to = new Node(command.to);
        if (graph.containsVertex(from) && graph.containsVertex(to)) {
            final List<Edge> edges = graph.getEdges().stream().
                    filter(
                            edge -> edge.from.equals(command.from) && edge.to.equals(command.to)
                    ).collect(toList());
            for (final Edge edge : edges) {
                if (graph.containsEdge(edge)) {
                    graph.removeEdge(edge);
                } else {
                    LOGGER.warn("Edge already removed");
                }
            }
            return EDGE_REMOVED;
        } else {
            return NODE_NOT_FOUND;
        }
    }

    private String handleShortestPath(final ShortestPath command) {
        final Node from = new Node(command.from);
        final Node to = new Node(command.to);
        if (graph.containsVertex(from) && graph.containsVertex(to)) {
            final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);
            final Number distance = shortestPath.getDistance(from, to);
            final int weight = distance == null ? Integer.MAX_VALUE : distance.intValue();
            return format(SHORTEST_PATH, weight);
        } else {
            return NODE_NOT_FOUND;
        }
    }

    private String handleCloserThan(final CloserThan command) {
        final Node start = new Node(command.node);
        if (graph.containsVertex(start)) {
            final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);
            final Predicate<Node> predicate =
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
            final VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<>(predicate);
            final Graph<Node, Edge> closerThan = filter.transform(graph);
            final List<String> nodes = closerThan.getVertices().stream().map(n -> n.name).collect(toList());
            return Messages.closerThan(nodes);
        } else {
            return NODE_NOT_FOUND;
        }
    }
}
