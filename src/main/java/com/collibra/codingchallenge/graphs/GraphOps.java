package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;

final class GraphOps {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphOps.class);

    static Graph<Node, Edge> createGraph() {
        return new DirectedSparseMultigraph<>();
    }


    static boolean addNode(final Graph<Node, Edge> graph, final String node) {
        final Node n = new Node(node);
        return graph.addVertex(n);
    }

    static boolean removeNode(final Graph<Node, Edge> graph, final String node) {
        final Node n = new Node(node);
        return graph.removeVertex(n);
    }

    
    static boolean addEdge(final Graph<Node, Edge> graph, final String from, final String to, final int weight) {

        final Node f = new Node(from);
        final Node t = new Node(to);

        if (!graph.containsVertex(f) || !graph.containsVertex(t)) {

            if (!graph.containsVertex(f)) {
                LOGGER.warn("Starting node not found - '{}'", from);
            }
            if (!graph.containsVertex(f)) {
                LOGGER.warn("Ending node not found - '{}'", to);
            }

            return false;
        }

        final Edge e = new Edge(weight, from, to);

        if (graph.containsEdge(e)) {
            LOGGER.warn("Edge already exists - {}", e);
        } else {
            graph.addEdge(e, f, t, DIRECTED);
        }

        return true;
    }
}
