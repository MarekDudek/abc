package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

public final class GraphOps {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphOps.class);


    public static Graph<Node, Edge> graph() {
        return new DirectedSparseMultigraph<>();
    }

    public static Node node(final String id) {
        return new Node(id);
    }

    static Edge edge(final int weight, final String start, final String end) {
        return new Edge(weight, start, end);
    }

    public static boolean addNode(final Graph<Node, Edge> graph, final String node) {

        final Node n = node(node);
        final boolean added = graph.addVertex(n);

        if (!added) {
            LOGGER.info("Node not added - {}", n);
        }

        return added;
    }

    public static boolean removeNode(final Graph<Node, Edge> graph, final String node) {

        final Node n = node(node);
        final boolean removed = graph.removeVertex(n);

        if (!removed) {
            LOGGER.info("Node not removed - {}", n);
        }

        return removed;
    }


    public static boolean addEdge(final Graph<Node, Edge> graph, final int weight, final String start, final String end) {

        final Node st = node(start);

        if (!graph.containsVertex(st)) {
            LOGGER.info("Starting node not found - '{}'", start);
            return false;
        }

        final Node en = node(end);

        if (!graph.containsVertex(en)) {
            LOGGER.info("Ending node not found - '{}'", end);
            return false;
        }

        final Edge e = new Edge(weight, start, end);

        if (graph.containsEdge(e)) {
            LOGGER.info("Edge already exists - {}", e);
        } else {
            graph.addEdge(e, st, en, DIRECTED);
        }

        return true;
    }

    public static boolean removeEdge(final Graph<Node, Edge> graph, final String start, final String end) {

        final Node st = node(start);

        if (!graph.containsVertex(st)) {
            LOGGER.info("Starting node not found - '{}'", start);
            return false;
        }

        final Node en = node(end);

        if (!graph.containsVertex(en)) {
            LOGGER.info("Ending node not found - '{}'", end);
            return false;
        }

        final List<Edge> edges = graph.getEdges().stream().filter(
                edge -> edge.start.equals(start) && edge.end.equals(end)
        ).collect(toList());

        if (edges.isEmpty()) {
            LOGGER.info("No edge between {} and {} end remove", st, en);
        }

        for (final Edge edge : edges) {
            if (graph.containsEdge(edge)) {
                graph.removeEdge(edge);
            } else {
                LOGGER.info("Edge already removed - {}", edge);
            }
        }

        return true;
    }


    public static Optional<Integer> shortestPath(final Graph<Node, Edge> graph, final String start, final String end) {

        final Node st = node(start);

        if (!graph.containsVertex(st)) {
            LOGGER.info("Starting node not found - '{}'", start);
            return Optional.empty();
        }

        final Node en = node(end);

        if (!graph.containsVertex(en)) {
            LOGGER.info("Ending node not found - '{}'", end);
            return Optional.empty();
        }

        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);
        final Number distance = shortestPath.getDistance(st, en);
        final int weight = nodesConnected(distance) ? distance.intValue() : Integer.MAX_VALUE;

        return Optional.of(weight);
    }

    public static Optional<List<String>> closerThan(final Graph<Node, Edge> graph, final int threshold, final String start) {

        final Node st = node(start);

        if (!graph.containsVertex(st)) {
            LOGGER.info("Starting node not found - '{}'", start);
            return Optional.empty();
        }

        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);

        final Predicate<Node> closerThanThreshold =
                n -> {
                    if (n.equals(st)) {
                        return false;
                    }
                    final Number distance = shortestPath.getDistance(st, n);
                    final int weight = nodesConnected(distance) ? distance.intValue() : Integer.MAX_VALUE;
                    return weight < threshold;
                };

        final VertexPredicateFilter<Node, Edge> nodeFilter = new VertexPredicateFilter<>(closerThanThreshold);
        final Graph<Node, Edge> closerNodes = nodeFilter.transform(graph);

        final List<String> names = closerNodes.getVertices().stream().map(n -> n.name).sorted(naturalOrder()).collect(toList());

        return Optional.of(names);
    }

    private static boolean nodesConnected(final Number distance) {
        return distance != null;
    }
}
