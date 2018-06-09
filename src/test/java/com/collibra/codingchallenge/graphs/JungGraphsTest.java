package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

    @Test
    public void adding_and_removing_edges() {
        // given
        final String from = "from";
        final String to = "to";
        final int weight = 10;
        // when
        final DirectedSparseMultigraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<>();
        // then
        assertThat(graph.containsEdge(new MyEdge(from, to, weight)), is(false));
        assertThat(graph.containsEdge(new MyEdge(from, to, 23)), is(false)); // TODO
        // when
        graph.addVertex(new MyNode(from));
        graph.addVertex(new MyNode(to));
        final boolean added = graph.addEdge(new MyEdge(from, to, weight), new MyNode(from), new MyNode(to), DIRECTED);
        // then
        assertThat(added, is(true));
        assertThat(graph.containsEdge(new MyEdge(from, to, weight)), is(true));
        // when
        final boolean addedAgain = graph.addEdge(new MyEdge(from, to, weight), new MyNode(from), new MyNode(to), DIRECTED);
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = graph.removeEdge(new MyEdge(from, to, weight));
        // then
        assertThat(removed, is(true));
        // when
        final boolean removedAgain = graph.removeEdge(new MyEdge(from, to, weight));
        // then
        assertThat(removedAgain, is(false));
        // when
        final boolean addedYetAgain = graph.addEdge(new MyEdge(from, to, weight), new MyNode(from), new MyNode(to), DIRECTED);
        // then
        assertThat(addedYetAgain, is(true));
        // when
        final boolean fromRemoved = graph.removeVertex(new MyNode(from));
        // then
        assertThat(fromRemoved, is(true));
        assertThat(graph.containsEdge(new MyEdge(from, to, weight)), is(false));
        assertThat(graph.containsVertex(new MyNode(from)), is(false));
        assertThat(graph.containsVertex(new MyNode(to)), is(true));
        // when
        final boolean toRemoved = graph.removeVertex(new MyNode(to));
        // then
        assertThat(toRemoved, is(true));
        assertThat(graph.containsVertex(new MyNode(to)), is(false));
    }

    @Test
    public void shortest_path() {
        // given
        final String start = "start";
        final String end = "end";
        final String other = "other";
        final DirectedSparseMultigraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new MyNode(start));
        graph.addVertex(new MyNode(end));
        graph.addVertex(new MyNode(other));
        graph.addEdge(new MyEdge(start, end, 10), new MyNode(start), new MyNode(end), DIRECTED);
        graph.addEdge(new MyEdge(start, other, 4), new MyNode(start), new MyNode(other), DIRECTED);
        graph.addEdge(new MyEdge(other, end, 3), new MyNode(other), new MyNode(end), DIRECTED);
        // when
        final DijkstraShortestPath<MyNode, MyEdge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        final Number distance = algorithm.getDistance(new MyNode(start), new MyNode(end));
        // then
        assertThat(distance, is(7.0));
    }

    @Test
    public void shortest_path__no_connection() {
        // given
        final String a = "a";
        final String b = "b";
        final DirectedSparseMultigraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new MyNode(a));
        graph.addVertex(new MyNode(b));
        // when
        final DijkstraShortestPath<MyNode, MyEdge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        final Number distance = algorithm.getDistance(new MyNode(a), new MyNode(b));
        // then
        assertThat(distance, nullValue());
    }

    @Test
    public void shortest_path__node_not_found() {
        // given
        final String a = "a";
        final String b = "b";
        final DirectedSparseMultigraph<MyNode, MyEdge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new MyNode(a));
        // when
        final DijkstraShortestPath<MyNode, MyEdge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        try {
            algorithm.getDistance(new MyNode(a), new MyNode(b));
            fail();
        } catch (final IllegalArgumentException ignored) {
        }
        // then
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