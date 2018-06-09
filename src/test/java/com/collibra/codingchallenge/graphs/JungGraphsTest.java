package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
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
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        // then
        assertThat(graph.containsVertex(new Node(id)), is(false));
        // when
        final boolean added = graph.addVertex(new Node(id));
        // then
        assertThat(added, is(true));
        assertThat(graph.containsVertex(new Node(id)), is(true));
        // when
        final boolean addedAgain = graph.addVertex(new Node(id));
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = graph.removeVertex(new Node(id));
        // then
        assertThat(removed, is(true));
        assertThat(graph.containsVertex(new Node(id)), is(false));
        // when
        final boolean removedAgain = graph.removeVertex(new Node(id));
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
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        // then
        assertThat(graph.containsEdge(new Edge(from, to, weight)), is(false));
        assertThat(graph.containsEdge(new Edge(from, to, 23)), is(false)); // TODO
        // when
        graph.addVertex(new Node(from));
        graph.addVertex(new Node(to));
        final boolean added = graph.addEdge(new Edge(from, to, weight), new Node(from), new Node(to), DIRECTED);
        // then
        assertThat(added, is(true));
        assertThat(graph.containsEdge(new Edge(from, to, weight)), is(true));
        // when
        final boolean addedAgain = graph.addEdge(new Edge(from, to, weight), new Node(from), new Node(to), DIRECTED);
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = graph.removeEdge(new Edge(from, to, weight));
        // then
        assertThat(removed, is(true));
        // when
        final boolean removedAgain = graph.removeEdge(new Edge(from, to, weight));
        // then
        assertThat(removedAgain, is(false));
        // when
        final boolean addedYetAgain = graph.addEdge(new Edge(from, to, weight), new Node(from), new Node(to), DIRECTED);
        // then
        assertThat(addedYetAgain, is(true));
        // when
        final boolean fromRemoved = graph.removeVertex(new Node(from));
        // then
        assertThat(fromRemoved, is(true));
        assertThat(graph.containsEdge(new Edge(from, to, weight)), is(false));
        assertThat(graph.containsVertex(new Node(from)), is(false));
        assertThat(graph.containsVertex(new Node(to)), is(true));
        // when
        final boolean toRemoved = graph.removeVertex(new Node(to));
        // then
        assertThat(toRemoved, is(true));
        assertThat(graph.containsVertex(new Node(to)), is(false));
    }

    @Test
    public void shortest_path() {
        // given
        final String start = "start";
        final String end = "end";
        final String other = "other";
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new Node(start));
        graph.addVertex(new Node(end));
        graph.addVertex(new Node(other));
        graph.addEdge(new Edge(start, end, 10), new Node(start), new Node(end), DIRECTED);
        graph.addEdge(new Edge(start, other, 4), new Node(start), new Node(other), DIRECTED);
        graph.addEdge(new Edge(other, end, 3), new Node(other), new Node(end), DIRECTED);
        // when
        final DijkstraShortestPath<Node, Edge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        final Number distance = algorithm.getDistance(new Node(start), new Node(end));
        // then
        assertThat(distance, is(7.0));
    }

    @Test
    public void shortest_path__no_connection() {
        // given
        final String a = "a";
        final String b = "b";
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new Node(a));
        graph.addVertex(new Node(b));
        // when
        final DijkstraShortestPath<Node, Edge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        final Number distance = algorithm.getDistance(new Node(a), new Node(b));
        // then
        assertThat(distance, nullValue());
    }

    @Test
    public void shortest_path__node_not_found() {
        // given
        final String a = "a";
        final String b = "b";
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new Node(a));
        // when
        final DijkstraShortestPath<Node, Edge> algorithm = new DijkstraShortestPath<>(graph, n -> n.weight);
        try {
            algorithm.getDistance(new Node(a), new Node(b));
            fail();
        } catch (final IllegalArgumentException ignored) {
        }
        // then
    }
}
