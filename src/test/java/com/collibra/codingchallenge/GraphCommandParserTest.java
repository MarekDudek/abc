package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.*;
import com.collibra.codingchallenge.graphs.GraphCommandParser;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class GraphCommandParserTest {

    @Test
    public void add_node() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("ADD NODE node");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new AddNode("node")));
    }

    @Test
    public void add_edge() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("ADD EDGE from to 23");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new AddEdge("from", "to", 23)));
    }

    @Test
    public void remove_node() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("REMOVE NODE node");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new RemoveNode("node")));
    }

    @Test
    public void remove_edge() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("REMOVE EDGE from to");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new RemoveEdge("from", "to")));
    }

    @Test
    public void shortest_path() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("SHORTEST PATH from to");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new ShortestPath("from", "to")));
    }

    @Test
    public void closer_than() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("CLOSER THAN 23 node");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new CloserThan(23, "node")));
    }
}
