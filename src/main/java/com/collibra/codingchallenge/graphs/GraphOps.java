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
import static java.util.stream.Collectors.toList;

public final class GraphOps {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphOps.class);


    public static Graph<Node, Edge> createGraph() {
        return new DirectedSparseMultigraph<>();
    }


    public static boolean addNode(final Graph<Node, Edge> graph, final String node) {

        final Node n = new Node(node);
        final boolean added = graph.addVertex(n);

        if (!added) {
            LOGGER.info("Node not added - {}", n);
        }

        return added;
    }

    public static boolean removeNode(final Graph<Node, Edge> graph, final String node) {

        final Node n = new Node(node);
        final boolean removed = graph.removeVertex(n);

        if (!removed) {
            LOGGER.info("Node not removed - {}", n);
        }

        return removed;
    }


    public static boolean addEdge(final Graph<Node, Edge> graph, final String from, final String to, final int weight) {

        final Node f = new Node(from);

        if (!graph.containsVertex(f)) {
            LOGGER.info("Starting node not found - '{}'", from);
            return false;
        }

        final Node t = new Node(to);

        if (!graph.containsVertex(t)) {
            LOGGER.info("Ending node not found - '{}'", to);
            return false;
        }

        final Edge e = new Edge(weight, from, to);

        if (graph.containsEdge(e)) {
            LOGGER.info("Edge already exists - {}", e);
        } else {
            graph.addEdge(e, f, t, DIRECTED);
        }

        return true;
    }

    public static boolean removeEdge(final Graph<Node, Edge> graph, final String from, final String to) {

        final Node f = new Node(from);

        if (!graph.containsVertex(f)) {
            LOGGER.info("Starting node not found - '{}'", from);
            return false;
        }

        final Node t = new Node(to);

        if (!graph.containsVertex(t)) {
            LOGGER.info("Ending node not found - '{}'", to);
            return false;
        }

        final List<Edge> edges = graph.getEdges().stream().filter(
                edge -> edge.from.equals(from) && edge.to.equals(to)
        ).collect(toList());

        if (edges.isEmpty()) {
            LOGGER.info("No edge between {} and {} to remove", f, t);
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


    public static Optional<Integer> shortestPath(final Graph<Node, Edge> graph, final String from, final String to) {

        final Node f = new Node(from);

        if (!graph.containsVertex(f)) {
            LOGGER.info("Starting node not found - '{}'", from);
            return Optional.empty();
        }

        final Node t = new Node(to);

        if (!graph.containsVertex(t)) {
            LOGGER.info("Ending node not found - '{}'", to);
            return Optional.empty();
        }

        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);
        final Number distance = shortestPath.getDistance(f, t);
        final int weight = nodesConnected(distance) ? distance.intValue() : Integer.MAX_VALUE;

        return Optional.of(weight);
    }

    public static Optional<List<String>> closerThan(final Graph<Node, Edge> graph, final int threshold, final String from) {

        final Node f = new Node(from);

        if (!graph.containsVertex(f)) {
            LOGGER.info("Starting node not found - '{}'", from);
            return Optional.empty();
        }

        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);

        final Predicate<Node> closerThanThreshold =
                n -> {
                    if (n.equals(f)) {
                        return false;
                    }
                    final Number distance = shortestPath.getDistance(f, n);
                    final int weight = nodesConnected(distance) ? distance.intValue() : Integer.MAX_VALUE;
                    return weight < threshold;
                };

        final VertexPredicateFilter<Node, Edge> nodeFilter = new VertexPredicateFilter<>(closerThanThreshold);
        final Graph<Node, Edge> closerNodes = nodeFilter.transform(graph);

        final List<String> names = closerNodes.getVertices().stream().map(n -> n.name).collect(toList());

        return Optional.of(names);
    }

    private static boolean nodesConnected(final Number distance) {
        return distance != null;
    }
}
