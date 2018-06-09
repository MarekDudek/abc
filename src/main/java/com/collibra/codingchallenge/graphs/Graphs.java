package com.collibra.codingchallenge.graphs;

import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.graphs.SimpleDirectedGraph;
import com.hendrix.erdos.types.Edge;
import com.hendrix.erdos.types.IVertex;
import com.hendrix.erdos.types.Vertex;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;

import static java.util.stream.Collectors.toList;

public final class Graphs {

    private interface FourPredicate<A, B, C, D> {
        boolean test(A a, B b, C c, D d);
    }

    private static final BiPredicate<IVertex, IVertex> NODES_EQUAL =
            (a, b) -> Objects.equals(a.getData(), b.getData());

    private static final FourPredicate<Edge, IVertex, IVertex, Optional<Integer>> EDGE_EQUALS =
            (e, from, to, weight) -> {
                final boolean f = NODES_EQUAL.test(e.getV1(), from);
                final boolean t = NODES_EQUAL.test(e.getV2(), to);
                final IntPredicate equal = w -> w == e.getWeight();
                final Optional<Boolean> w = weight.map(equal::test);
                return f && t && w.orElse(true);
            };


    public static SimpleDirectedGraph directedGraph() {
        return new SimpleDirectedGraph();
    }

    public static IVertex<String> node(final String id) {
        final IVertex<String> node = new Vertex<>();
        node.setData(id);
        return node;
    }

    public static boolean addNode(final AbstractGraph graph, final IVertex node) {
        return findNode(graph, node).map(
                found -> false
        ).orElseGet(
                () -> {
                    graph.addVertex(node);
                    return true;
                }
        );
    }

    public static boolean removeNode(final AbstractGraph graph, final IVertex query) {
        return findNode(graph, query).map(
                graph::removeVertex
        ).orElse(
                false
        );
    }

    public static boolean nodeExists(final AbstractGraph graph, final IVertex query) {
        return findNode(graph, query).isPresent();
    }

    private static Optional<IVertex> findNode(final AbstractGraph graph, final IVertex query) {
        return graph.vertices().stream().
                filter(
                        n -> NODES_EQUAL.test(n, query)
                ).
                findFirst();
    }


    public static boolean addEdge
            (
                    final AbstractGraph graph,
                    final IVertex<String> fromQuery,
                    final IVertex<String> toQuery,
                    final int weight
            ) {

        final Optional<Edge> edge = findEdge(graph, fromQuery, toQuery, Optional.of(weight));
        if (edge.isPresent()) {
            return false;
        } else {
            final Optional<IVertex> from = findNode(graph, fromQuery);
            final Optional<IVertex> to = findNode(graph, toQuery);
            if (from.isPresent() && to.isPresent()) {
                graph.addEdge(from.get(), to.get(), weight);
                return true;
            }
            return false;
        }
    }

    public static int removeEdges
            (
                    final AbstractGraph graph,
                    final IVertex<String> fromQuery,
                    final IVertex<String> toQuery
            ) {
        final List<Edge> edges = findEdges(graph, fromQuery, toQuery);
        edges.forEach(graph::removeEdge);
        return edges.size();
    }

    public static boolean edgeExists
            (
                    final AbstractGraph graph,
                    final IVertex<String> fromQuery,
                    final IVertex<String> toQuery
            ) {
        return findEdge(graph, fromQuery, toQuery, Optional.empty()).isPresent();
    }

    public static Boolean edgeExists
            (
                    final AbstractGraph graph,
                    final IVertex<String> fromQuery,
                    final IVertex<String> toQuery,
                    final int weightQuery
            ) {
        return findEdge(graph, fromQuery, toQuery, Optional.of(weightQuery)).isPresent();
    }

    private static Optional<Edge> findEdge
            (
                    final AbstractGraph graph,
                    final IVertex from,
                    final IVertex to,
                    final Optional<Integer> weight
            ) {
        return graph.edges().stream().
                filter(
                        e -> EDGE_EQUALS.test(e, from, to, weight)
                ).
                findFirst();
    }

    private static List<Edge> findEdges
            (
                    final AbstractGraph graph,
                    final IVertex fromQuery,
                    final IVertex toQuery
            ) {
        return graph.edges().stream().
                filter(
                        e -> EDGE_EQUALS.test(e, fromQuery, toQuery, Optional.empty())
                ).
                collect(toList());
    }
}
