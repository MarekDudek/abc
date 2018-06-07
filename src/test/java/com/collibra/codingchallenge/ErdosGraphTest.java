package com.collibra.codingchallenge;

import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.graphs.SimpleDirectedGraph;
import com.hendrix.erdos.interfaces.IData;
import com.hendrix.erdos.types.Edge;
import com.hendrix.erdos.types.IVertex;
import com.hendrix.erdos.types.Vertex;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;

import static com.hendrix.erdos.types.Edge.EDGE_DIRECTION.DIRECTED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class ErdosGraphTest {

    private static IVertex<String> vertex(final String id) {
        final IVertex<String> vertex = new Vertex<>();
        vertex.setData(id);
        return vertex;
    }

    private static Optional<IVertex> find(final AbstractGraph graph, final IData query) {
        for (final IVertex vertex : graph.vertices())
            if (Objects.equals(vertex.getData(), query.getData()))
                return Optional.of(vertex);
        return Optional.empty();
    }

    private static boolean exists(final AbstractGraph graph, final IData query) {
        return find(graph, query).isPresent();
    }

    private static void removeVertex(final AbstractGraph graph, final IData query) {
        find(graph, query).ifPresent(graph::removeVertex);
    }

    @Test
    public void add_node() {
        // when
        final SimpleDirectedGraph graph = new SimpleDirectedGraph();
        // then
        assertFalse(exists(graph, vertex("node")));
        // when
        graph.addVertex(vertex("node"));
        // then
        assertTrue(exists(graph, vertex("node")));
        // when
        removeVertex(graph, vertex("node"));
        // then
        assertFalse(exists(graph, vertex("node")));
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
