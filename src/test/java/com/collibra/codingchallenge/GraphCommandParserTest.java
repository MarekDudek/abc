package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.AddEdge;
import com.collibra.codingchallenge.commands.AddNode;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class GraphCommandParserTest {

    @Test
    public void add_node() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("ADD NODE sth");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new AddNode("sth")));
    }

    @Test
    public void add_edge() {
        // when
        final Optional<GraphCommand> command = GraphCommandParser.parse("ADD EDGE from to 23");
        // then
        assertTrue(command.isPresent());
        assertThat(command.get(), is(new AddEdge("from", "to", 23)));
    }
}
