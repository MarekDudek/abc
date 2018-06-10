package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Predicate;
import org.junit.Test;

import java.util.Optional;

import static com.collibra.codingchallenge.graphs.GraphOps.*;
import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
    public void closer_than_again() {
        // given
        final String start = "start";
        final String close = "close";
        final String further = "further";
        final String far = "far";
        final int threshold = 5;
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new Node(start));
        graph.addVertex(new Node(close));
        graph.addVertex(new Node(further));
        graph.addVertex(new Node(far));
        graph.addEdge(new Edge(1, start, close), new Node(start), new Node(close), DIRECTED);
        graph.addEdge(new Edge(2, close, further), new Node(close), new Node(further), DIRECTED);
        graph.addEdge(new Edge(5, further, far), new Node(further), new Node(far), DIRECTED);
        // when
        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, n -> n.weight);
        final Predicate<Node> predicate = node -> {
            if (node.equals(new Node(start))) {
                return false;
            }
            final Number distance = shortestPath.getDistance(new Node(start), node);
            return distance.intValue() < threshold;
        };
        final VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<>(predicate);
        final Graph<Node, Edge> transformed = filter.transform(graph);
        // then
        assertThat(transformed.getVertices(), containsInAnyOrder(new Node(close), new Node(further)));
    }

    @Test
    public void closer_than_example() {
        // given
        final String mark = "Mark";
        final String michael = "Michael";
        final String madeleine = "Madeleine";
        final String mufasa = "Mufasa";
        final int threshold = 8;
        // when
        final DirectedSparseMultigraph<Node, Edge> graph = new DirectedSparseMultigraph<>();
        graph.addVertex(new Node(mark));
        graph.addVertex(new Node(michael));
        graph.addVertex(new Node(madeleine));
        graph.addVertex(new Node(mufasa));
        graph.addEdge(new Edge(5, mark, michael), new Node(mark), new Node(michael), DIRECTED);
        graph.addEdge(new Edge(2, michael, madeleine), new Node(michael), new Node(madeleine), DIRECTED);
        graph.addEdge(new Edge(8, madeleine, mufasa), new Node(madeleine), new Node(mufasa), DIRECTED);
        // when
        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, e -> e.weight);
        final Predicate<Node> predicate =
                node -> {
                    if (node.equals(new Node(mark))) {
                        return false;
                    }
                    final Number distance = shortestPath.getDistance(new Node(mark), node);
                    if (distance == null) {
                        return false;
                    }
                    final int weight = distance.intValue();
                    return weight < threshold;
                };
        final VertexPredicateFilter<Node, Edge> filter = new VertexPredicateFilter<>(predicate);
        final Graph<Node, Edge> closer = filter.transform(graph);
        final String response = closer.getVertices().stream().
                map(
                        n -> n.name
                ).
                sorted(
                        naturalOrder()
                ).
                collect(
                        joining(",")
                );
        // then
        assertThat(response, is("Madeleine,Michael"));
    }
}
