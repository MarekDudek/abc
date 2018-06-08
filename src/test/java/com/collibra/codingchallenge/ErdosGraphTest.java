package com.collibra.codingchallenge;

import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.graphs.SimpleDirectedGraph;
import com.hendrix.erdos.types.Edge;
import com.hendrix.erdos.types.IVertex;
import com.hendrix.erdos.types.Vertex;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ErdosGraphTest {

    private static final BiPredicate<IVertex, IVertex> EQUALS =
            (a, b) -> Objects.equals(a.getData(), b.getData());

    private static IVertex<String> vertex(final String id) {
        final IVertex<String> vertex = new Vertex<>();
        vertex.setData(id);
        return vertex;
    }

    private static Optional<IVertex> findVertex(final AbstractGraph graph, final IVertex query) {
        for (final IVertex vertex : graph.vertices()) {
            if (EQUALS.test(query, vertex)) {
                return Optional.of(vertex);
            }
        }
        return Optional.empty();
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
        assertThat(addedAgain, is(false));
    }

    private static Optional<Edge> findEdge
            (
                    final AbstractGraph graph,
                    final IVertex from,
                    final IVertex to
            ) {
        for (final Edge e : graph.edges()) {
            final boolean f = EQUALS.test(e.getV1(), from);
            final boolean t = EQUALS.test(e.getV2(), to);
            if (f && t) {
                return Optional.of(e);
            }

        }
        return Optional.empty();
    }

    private static Optional<Edge> findEdge
            (
                    final AbstractGraph graph,
                    final IVertex from,
                    final IVertex to,
                    final int weight
            ) {
        for (final Edge e : graph.edges()) {
            final boolean f = EQUALS.test(e.getV1(), from);
            final boolean t = EQUALS.test(e.getV2(), to);
            final boolean w = e.getWeight() == weight;
            if (f && t && w) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    private boolean edgeExists
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to
            ) {
        return findEdge(graph, from, to).isPresent();
    }

    private Boolean edgeExists
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to,
                    final int weight
            ) {
        return findEdge(graph, from, to, weight).isPresent();
    }

    private static boolean addEdge
            (
                    final SimpleDirectedGraph graph,
                    final IVertex<String> from,
                    final IVertex<String> to,
                    final int weight
            ) {
        final Optional<IVertex> f = findVertex(graph, from);
        final Optional<IVertex> t = findVertex(graph, to);
        if (f.isPresent() && t.isPresent()) {
            final Optional<Edge> edge = findEdge(graph, f.get(), t.get());
            if (!edge.isPresent()) {
                graph.addEdge(f.get(), t.get(), weight);
                return true;
            }
        }
        return false;
    }
}
