package com.collibra.codingchallenge.graphs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.Test;

import java.util.function.Supplier;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class JGraphTGraphsTest {

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "node";
        // when
        final SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        // then
        assertThat(graph.containsVertex(id), is(false));
        // when
        final boolean added = graph.addVertex(id);
        // then
        assertThat(added, is(true));
        assertThat(graph.containsVertex(id), is(true));
        // when
        final boolean addedAgain = graph.addVertex(id);
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = graph.removeVertex(id);
        // then
        assertThat(removed, is(true));
        assertThat(graph.containsVertex(id), is(false));
        // when
        final boolean removedAgain = graph.removeVertex(id);
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
        final SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        // then
        final DefaultWeightedEdge defaultEdge = null;
        final Supplier<DefaultWeightedEdge> edgeSupplier = graph.getEdgeSupplier();
        final DefaultWeightedEdge edge = edgeSupplier.get();



        graph.containsEdge(defaultEdge);
    }
}
