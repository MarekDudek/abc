package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import static com.collibra.codingchallenge.graphs.ErdosGraphs.node;
import static com.collibra.codingchallenge.graphs.ErdosGraphs.removeNode;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class JungGraphsTest {

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "node";
        // when
        final DirectedSparseMultigraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<>();
        // then
        assertThat(graph.containsVertex(new MyNode(id)), is(false));
        // when
        final boolean added = graph.addVertex(new MyNode(id));
        // then
        assertThat(added, is(true));
        assertThat(graph.containsVertex(new MyNode(id)), is(true));
        // when
        final boolean addedAgain = graph.addVertex(new MyNode(id));
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = graph.removeVertex(new MyNode(id));
        // then
        assertThat(removed, is(true));
        assertThat(graph.containsVertex(new MyNode(id)), is(false));
        // when
        final boolean removedAgain = graph.removeVertex(new MyNode(id));
        // then
        assertThat(removedAgain, is(false));
    }
}


@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class MyNode {
    public final String name;
}

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class MyEdge {
    public final String fromName;
    public final String toName;
    public final int weight;
}