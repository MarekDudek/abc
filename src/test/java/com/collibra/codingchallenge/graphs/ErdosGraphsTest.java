package com.collibra.codingchallenge.graphs;

import com.hendrix.erdos.graphs.IDirectedGraph;
import org.junit.Test;

import java.util.Optional;

import static com.collibra.codingchallenge.graphs.ErdosGraphs.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ErdosGraphsTest {

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "node";
        // when
        final IDirectedGraph graph = directedGraph();
        // then
        assertThat(nodeExists(graph, node(id)), is(false));
        // when
        final boolean added = addNode(graph, node(id));
        // then
        assertThat(added, is(true));
        assertThat(nodeExists(graph, node(id)), is(true));
        // when
        final boolean addedAgain = addNode(graph, node(id));
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = removeNode(graph, node(id));
        // then
        assertThat(removed, is(true));
        assertThat(nodeExists(graph, node(id)), is(false));
        // when
        final boolean removedAgain = removeNode(graph, node(id));
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
        final IDirectedGraph graph = directedGraph();
        // then
        assertThat(edgeExists(graph, node(from), node(to)), is(false));
        assertThat(edgeExists(graph, node(from), node(to), weight), is(false));
        // when
        addNode(graph, node(from));
        addNode(graph, node(to));
        final boolean added = addEdge(graph, node(from), node(to), weight);
        // then
        assertThat(added, is(true));
        assertThat(edgeExists(graph, node(from), node(to)), is(true));
        assertThat(edgeExists(graph, node(from), node(to), weight), is(true));
        // when
        final boolean addedAgain = addEdge(graph, node(from), node(to), weight);
        // then
        assertThat(addedAgain, is(false));
        // when
        final int removed = removeEdges(graph, node(from), node(to));
        // then
        assertThat(removed, is(1));
        // when
        final int removedAgain = removeEdges(graph, node(from), node(to));
        // then
        assertThat(removedAgain, is(0));
        // when
        final boolean addedYetAgain = addEdge(graph, node(from), node(to), weight);
        // then
        assertThat(addedYetAgain, is(true));
        // when
        final boolean fromRemoved = removeNode(graph, node(from));
        // then
        assertThat(fromRemoved, is(true));
        assertThat(edgeExists(graph, node(from), node(to)), is(false));
        assertThat(nodeExists(graph, node(from)), is(false));
        assertThat(nodeExists(graph, node(to)), is(true));
        // when
        final boolean toRemoved = removeNode(graph, node(to));
        // then
        assertThat(toRemoved, is(true));
        assertThat(nodeExists(graph, node(to)), is(false));
    }

    @Test
    public void shortest_path() {
        // given
        final String start = "start";
        final String end = "end";
        final String other = "other";
        final IDirectedGraph graph = directedGraph();
        addNode(graph, node(start));
        addNode(graph, node(end));
        addNode(graph, node(other));
        addEdge(graph, node(start), node(end), 10);
        addEdge(graph, node(start), node(other), 4);
        addEdge(graph, node(other), node(end), 3);
        // when
        final Optional<Integer> weight = shortestPath(graph, node(start), node(end));
        // then
        assertThat(weight.isPresent(), is(true));
        assertThat(weight, is(Optional.of(7)));
    }

    @Test
    public void shortest_path__no_connection() {
        // given
        final String a = "a";
        final String b = "b";
        final IDirectedGraph graph = directedGraph();
        addNode(graph, node(a));
        addNode(graph, node(b));
        // when
        final Optional<Integer> weight = shortestPath(graph, node(a), node(b));
        // then
        assertThat(weight, is(Optional.of(Integer.MAX_VALUE)));
    }

    @Test
    public void shortest_path__node_not_found() {
        // given
        final String a = "a";
        final String b = "b";
        final IDirectedGraph graph = directedGraph();
        addNode(graph, node(a));
        // when
        final Optional<Integer> weight = shortestPath(graph, node(a), node(b));
        // then
        assertThat(weight.isPresent(), is(false));
    }
}
