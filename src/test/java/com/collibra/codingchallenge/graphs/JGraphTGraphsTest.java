package com.collibra.codingchallenge.graphs;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class JGraphTGraphsTest {

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "node";
        // when
        final DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
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
}
