package com.collibra.codingchallenge;

import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.graphs.SimpleDirectedGraph;
import com.hendrix.erdos.types.Edge;
import com.hendrix.erdos.types.IVertex;
import com.hendrix.erdos.types.Vertex;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static com.hendrix.erdos.types.Edge.EDGE_DIRECTION.DIRECTED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ErdosGraphTest {

    private static IVertex<String> vertex(final String id) {
        final IVertex<String> vertex = new Vertex<>();
        vertex.setData(id);
        return vertex;
    }

    private static Optional<IVertex> find(final AbstractGraph graph, final IVertex query) {
        for (final IVertex vertex : graph.vertices()) {
            if (Objects.equals(vertex.getData(), query.getData())) {
                return Optional.of(vertex);
            }
        }
        return Optional.empty();
    }

    private static boolean exists(final AbstractGraph graph, final IVertex query) {
        return find(graph, query).isPresent();
    }

    private static boolean addVertex(final AbstractGraph graph, final IVertex vertex) {
        return find(graph, vertex).map(
                found -> false
        ).orElseGet(
                () -> {
                    graph.addVertex(vertex);
                    return true;
                }
        );
    }

    private static boolean removeVertex(final AbstractGraph graph, final IVertex query) {
        return find(graph, query).map(
                graph::removeVertex
        ).orElse(
                false
        );
    }

    @Test
    public void add_node() {
        // given
        final String id = "node";
        // when
        final SimpleDirectedGraph graph = new SimpleDirectedGraph();
        // then
        assertThat(exists(graph, vertex(id)), is(false));
        // when
        final boolean added = addVertex(graph, vertex(id));
        // then
        assertThat(added, is(true));
        assertThat(exists(graph, vertex(id)), is(true));
        // when
        final boolean addedAgain = addVertex(graph, vertex(id));
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = removeVertex(graph, vertex(id));
        // then
        assertThat(removed, is(true));
        assertThat(exists(graph, vertex(id)), is(false));
        // when
        final boolean removedAgain = removeVertex(graph, vertex(id));
        // then
        assertThat(removedAgain, is(false));
    }

    @Test
    @Ignore
    public void add_edge() {
        // when
        final SimpleDirectedGraph graph = new SimpleDirectedGraph();
        // then
        // ...
        // when
        final IVertex<String> node1 = vertex("node1");
        final IVertex<String> node2 = vertex("node2");
        graph.addVertex(node1);
        graph.addVertex(node2);
        final Edge edge = new Edge(vertex("node1"), vertex("node2"), DIRECTED, 23);
        edge.setData("node1-node2-23");
        graph.addEdge(edge);
        // then
        //graph.edges().parallelStream()
        // when


    }
}
