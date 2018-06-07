package com.collibra.codingchallenge;

import com.hendrix.erdos.graphs.AbstractGraph;
import com.hendrix.erdos.graphs.SimpleDirectedGraph;
import com.hendrix.erdos.interfaces.IData;
import com.hendrix.erdos.types.IVertex;
import com.hendrix.erdos.types.Vertex;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class ErdosGraphTest {

    private static boolean exists(final AbstractGraph graph, final IData query) {
        return graph.vertices().parallelStream().
                reduce(
                        false,
                        (exists, vertex) -> exists || Objects.equals(vertex.getData(), query.getData()),
                        (exists1, exists2) -> exists1 || exists2
                );
    }

    private static IVertex<String> vertex(final String id) {
        final IVertex<String> vertex = new Vertex<>();
        vertex.setData(id);
        return vertex;
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
    }

    @Test
    public void add_edge() {
        final SimpleDirectedGraph graph = new SimpleDirectedGraph();
        final Vertex<Object> node1 = new Vertex<>("node1");
        final Vertex<Object> node2 = new Vertex<>("node2");
        graph.addVertex(node1);
        graph.addVertex(node2);
        graph.addEdge(node1, node2);
        graph.print();
    }
}
