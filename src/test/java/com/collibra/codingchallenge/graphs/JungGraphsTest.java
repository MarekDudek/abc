package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.Test;

public final class JungGraphsTest {

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "node";
        // when
        final DirectedSparseMultigraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<>();
        // then
        graph.containsVertex(new MyNode(id));
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