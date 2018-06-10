package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

final class GraphOps {

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
}
