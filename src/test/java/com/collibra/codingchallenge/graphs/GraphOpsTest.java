package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.graph.Graph;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.collibra.codingchallenge.graphs.GraphOps.*;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class GraphOpsTest {

    @Test
    public void adding_and_removing_nodes() {
        // given
        final String id = "sth";
        // when
        final Graph<Node, Edge> graph = graph();
        // then
        assertThat(graph.containsVertex(node(id)), is(false));
        // when
        final boolean added = addNode(graph, id);
        // then
        assertThat(added, is(true));
        assertThat(graph.containsVertex(node(id)), is(true));
        // when
        final boolean addedAgain = addNode(graph, id);
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = removeNode(graph, id);
        // then
        assertThat(removed, is(true));
        assertThat(graph.containsVertex(node(id)), is(false));
        // when
        final boolean removedAgain = removeNode(graph, id);
        // then
        assertThat(removedAgain, is(false));
    }


    @Test
    public void adding_and_removing_edges() {
        // given
        final String start = "start";
        final String end = "end";
        final int weight = 10;
        // when
        final Graph<Node, Edge> graph = graph();
        // then
        assertThat(graph.containsEdge(edge(weight, start, end)), is(false));
        // when
        addNode(graph, start);
        addNode(graph, end);
        final boolean added = addEdge(graph, weight, start, end);
        // then
        assertThat(added, is(true));
        assertThat(graph.containsEdge(edge(weight, start, end)), is(true));
        // when
        final boolean addedAgain = addEdge(graph, weight, start, end);
        // then
        assertThat(addedAgain, is(true));
        // when
        final boolean removed = removeEdge(graph, start, end);
        // then
        assertThat(removed, is(true));
        // when
        final boolean removedAgain = removeEdge(graph, start, end);
        // then
        assertThat(removedAgain, is(true));
        // when
        final boolean addedYetAgain = addEdge(graph, weight, start, end);
        // then
        assertThat(addedYetAgain, is(true));
        assertThat(graph.containsEdge(edge(weight, start, end)), is(true));
        // when
        final boolean startRemoved = removeNode(graph, start);
        // then
        assertThat(startRemoved, is(true));
        assertThat(graph.containsEdge(edge(weight, start, end)), is(false));
        assertThat(graph.containsVertex(node(start)), is(false));
        assertThat(graph.containsVertex(node(end)), is(true));
        // when
        final boolean endRemoved = removeNode(graph, end);
        // then
        assertThat(endRemoved, is(true));
        assertThat(graph.containsVertex(node(end)), is(false));
    }

    @Test
    public void shortest_path() {
        // given
        final String start = "start";
        final String middle = "middle";
        final String end = "end";
        final Graph<Node, Edge> graph = graph();
        addNode(graph, start);
        addNode(graph, middle);
        addNode(graph, end);
        addEdge(graph, 10, start, end);
        addEdge(graph, 4, start, middle);
        addEdge(graph, 3, middle, end);
        // when
        final Optional<Integer> weight = shortestPath(graph, start, end);
        // then
        assertThat(weight, is(Optional.of(7)));
    }

    @Test
    public void shortest_path__between_not_connected_nodes() {
        // given
        final String island = "island";
        final String continent = "continent";
        final Graph<Node, Edge> graph = graph();
        addNode(graph, island);
        addNode(graph, continent);
        // when
        final Optional<Integer> weight = shortestPath(graph, island, continent);
        // then
        assertThat(weight, is(Optional.of(Integer.MAX_VALUE)));
    }

    @Test
    public void shortest_path__between_nodes_that_do_not_exist() {
        // given
        final String exists = "exists";
        final String doesNot = "does not";
        final Graph<Node, Edge> graph = graph();
        addNode(graph, exists);
        // when
        final Optional<Integer> weight = shortestPath(graph, exists, doesNot);
        // then
        assertThat(weight.isPresent(), is(false));
    }

    @Test
    public void closer_than() {
        // given
        final String start = "start";
        final String close = "close";
        final String further = "further";
        final String far = "far";
        final int threshold = 9;
        final Graph<Node, Edge> graph = graph();
        addNode(graph, start);
        addNode(graph, close);
        addNode(graph, further);
        addNode(graph, far);
        addEdge(graph, 2, start, close);
        addEdge(graph, 3, close, further);
        addEdge(graph, 4, further, far);
        // when
        final Optional<List<String>> nodes = closerThan(graph, threshold, start);
        // then
        assertThat(nodes.isPresent(), is(true));
        assertThat(nodes, is(Optional.of(asList(close, further))));
    }

    @Test
    public void closer_than__collibra_example() {
        // given
        final String mark = "Mark";
        final String michael = "Michael";
        final String madeleine = "Madeleine";
        final String mufasa = "Mufasa";
        final int threshold = 8;
        final Graph<Node, Edge> graph = graph();
        addNode(graph, mark);
        addNode(graph, michael);
        addNode(graph, madeleine);
        addNode(graph, mufasa);
        addEdge(graph, 5, mark, michael);
        addEdge(graph, 2, michael, madeleine);
        addEdge(graph, 8, madeleine, mufasa);
        // when
        final Optional<List<String>> nodes = closerThan(graph, threshold, mark);
        // then
        assertThat(nodes.isPresent(), is(true));
        assertThat(nodes, is(Optional.of(asList(madeleine, michael))));
    }
}
