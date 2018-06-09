package com.collibra.codingchallenge.graphs;

import edu.uci.ics.jung.algorithms.filters.VertexPredicateFilter;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import org.apache.commons.collections15.Predicate;
import org.junit.Test;

import static edu.uci.ics.jung.graph.util.EdgeType.DIRECTED;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.*;
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
        assertThat(graph.containsEdge(new Edge(weight, from, to)), is(false));
        assertThat(graph.containsEdge(new Edge(23, from, to)), is(false)); // TODO
        // when
        graph.addVertex(new Node(from));
        graph.addVertex(new Node(to));
        final boolean added = graph.addEdge(new Edge(weight, from, to), new Node(from), new Node(to), DIRECTED);
        // then
        assertThat(added, is(true));
        assertThat(graph.containsEdge(new Edge(weight, from, to)), is(true));
        // when
        final boolean addedAgain = graph.addEdge(new Edge(weight, from, to), new Node(from), new Node(to), DIRECTED);
        // then
        assertThat(addedAgain, is(false));
        // when
        final boolean removed = graph.removeEdge(new Edge(weight, from, to));
        // then
        assertThat(removed, is(true));
        // when
        final boolean removedAgain = graph.removeEdge(new Edge(weight, from, to));
        // then
        assertThat(removedAgain, is(false));
        // when
        final boolean addedYetAgain = graph.addEdge(new Edge(weight, from, to), new Node(from), new Node(to), DIRECTED);
        // then
        assertThat(addedYetAgain, is(true));
        // when
        final boolean fromRemoved = graph.removeVertex(new Node(from));
        // then
        assertThat(fromRemoved, is(true));
        assertThat(graph.containsEdge(new Edge(weight, from, to)), is(false));
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
        graph.addEdge(new Edge(10, start, end), new Node(start), new Node(end), DIRECTED);
        graph.addEdge(new Edge(4, start, other), new Node(start), new Node(other), DIRECTED);
        graph.addEdge(new Edge(3, other, end), new Node(other), new Node(end), DIRECTED);
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
        final DijkstraShortestPath<Node, Edge> shortestPath = new DijkstraShortestPath<>(graph, n -> n.weight);
        try {
            shortestPath.getDistance(new Node(a), new Node(b));
            fail();
        } catch (final IllegalArgumentException ignored) {
        }
        // then
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
