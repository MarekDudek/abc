package com.collibra.codingchallenge;

import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.graphs.SimpleDirectedGraph;
import com.hendrix.erdos.types.Edge;
import com.hendrix.erdos.types.IVertex;
import com.hendrix.erdos.types.Vertex;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ErdosGraphTest {

    private static final BiPredicate<IVertex, IVertex> VERTICES_EQUAL =
            (a, b) -> Objects.equals(a.getData(), b.getData());

    private static IVertex<String> vertex(final String id) {
        final IVertex<String> vertex = new Vertex<>();
        vertex.setData(id);
        return vertex;
    }

    private static Optional<IVertex> findVertex(final AbstractGraph graph, final IVertex query) {
        return graph.vertices().stream().
                filter(
                        v -> VERTICES_EQUAL.test(v, query)
                ).
                findFirst();
    }

    private static Optional<IVertex> findVertex(final AbstractGraph graph, final String query) {
        for (final IVertex vertex : graph.vertices()) {
            if (Objects.equals(query, vertex.getData())) {
                return Optional.of(vertex);
            }
        }
        return Optional.empty();
    }

    private static boolean vertexExists(final AbstractGraph graph, final IVertex query) {
        return findVertex(graph, query).isPresent();
    }

    private static boolean addVertex(final AbstractGraph graph, final IVertex vertex) {
        return findVertex(graph, vertex).map(
                found -> false
        ).orElseGet(
                () -> {
                    graph.addVertex(vertex);
                    return true;
                }
        );
    }

    private static boolean removeVertex(final AbstractGraph graph, final IVertex query) {
        return findVertex(graph, query).map(
                graph::removeVertex
        ).orElse(
                false
        );
    }

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "node";
        // when
        final SimpleDirectedGraph graph = new SimpleDirectedGraph();
        // then
        assertThat(vertexExists(graph, vertex(id)), is(false));
        // when
        final boolean added = addVertex(graph, vertex(id));
        // then
        assertThat(added, is(true));
        assertThat(vertexExists(graph, vertex(id)), is(true));
        // when
        final boolean addedAgain = addVertex(graph, vertex(id));
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = removeVertex(graph, vertex(id));
        // then
        assertThat(removed, is(true));
        assertThat(vertexExists(graph, vertex(id)), is(false));
        // when
        final boolean removedAgain = removeVertex(graph, vertex(id));
        // then
        assertThat(removedAgain, is(false));
    }

    @Test
    public void adding_and_removing_edges() {
        // given
        final String from = "from";
        final String to = "to";
        final int weight = 10;
        // when
        final SimpleDirectedGraph graph = new SimpleDirectedGraph();
        // then
        assertThat(edgeExists(graph, vertex(from), vertex(to)), is(false));
        assertThat(edgeExists(graph, vertex(from), vertex(to), weight), is(false));
        // when
        addVertex(graph, vertex(from));
        addVertex(graph, vertex(to));
        final boolean added = addEdge(graph, vertex(from), vertex(to), weight);
        // then
        assertThat(added, is(true));
        assertThat(edgeExists(graph, vertex(from), vertex(to)), is(true));
        assertThat(edgeExists(graph, vertex(from), vertex(to), weight), is(true));
        // when
        final boolean addedAgain = addEdge(graph, vertex(from), vertex(to), weight);
        // then
        assertThat(addedAgain, is(false));
        // when
        final int removed = removeEdge(graph, vertex(from), vertex(to));
        // then
        assertThat(removed, is(1));
        // when
        final int removedAgain = removeEdge(graph, vertex(from), vertex(to));
        // then
        assertThat(removedAgain, is(0));
        // when
        final boolean addedYetAgain = addEdge(graph, vertex(from), vertex(to), weight);
        // then
        assertThat(addedYetAgain, is(true));
        // when
        final boolean fromRemoved = removeVertex(graph, vertex(from));
        // then
        assertThat(fromRemoved, is(true));
        assertThat(edgeExists(graph, vertex(from), vertex(to)), is(false));
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
                    final IVertex from,
                    final IVertex to
            ) {
        return graph.edges().stream().
                filter(
                        e -> EDGE_EQUALS.test(e, from, to, Optional.empty())
                ).
                collect(toList());
    }

    private static final FourPredicate<Edge, IVertex, IVertex, Optional<Integer>> EDGE_EQUALS =
            (e, from, to, weight) -> {
                final boolean f = VERTICES_EQUAL.test(e.getV1(), from);
                final boolean t = VERTICES_EQUAL.test(e.getV2(), to);
                final IntPredicate equal = w -> w == e.getWeight();
                final Optional<Boolean> w = weight.map(equal::test);
                return f && t && w.orElse(true);
            };


    interface FourPredicate<A, B, C, D> {
        boolean test(A a, B b, C c, D d);
    }

    private boolean edgeExists
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to
            ) {
        return findEdge(graph, from, to, Optional.empty()).isPresent();
    }

    private Boolean edgeExists
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to,
                    final int weight
            ) {
        return findEdge(graph, from, to, Optional.of(weight)).isPresent();
    }

    private static boolean addEdge
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to,
                    final int weight
            ) {

        final Optional<Edge> edge = findEdge(graph, from, to, Optional.of(weight));
        if (!edge.isPresent()) {
            final Optional<IVertex> f = findVertex(graph, from);
            final Optional<IVertex> t = findVertex(graph, to);
            if (f.isPresent() && t.isPresent()) {
                graph.addEdge(f.get(), t.get(), weight);
                return true;
            }
        }
        return false;
    }


    private int removeEdge
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to
            ) {
        final List<Edge> edges = findEdges(graph, from, to);
        edges.forEach(graph::removeEdge);
        return edges.size();
    }
}
