package com.collibra.codingchallenge;

import com.collibra.codingchallenge.commands.AddEdge;
import com.collibra.codingchallenge.commands.AddNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;

final class GraphCommandParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphCommandParser.class);

    private static final Pattern REMOVE_NODE = Pattern.compile("REMOVE NODE (.*)");
    private static final Pattern EDGE_EDGE = Pattern.compile("REMOVE EDGE (.*) (.*)");

    private static final Pattern SHORTEST_PATH = Pattern.compile("SHORTEST PATH (.*) (.*)");
    private static final Pattern CLOSER_THAN = Pattern.compile("CLOSER THAN (.*) (.*)");

    private enum Producer {

        ADD_NODE(
                Pattern.compile("ADD NODE (.*)")
        ) {
            @Override
            Optional<GraphCommand> produce(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node = matcher.group(1);
                    final GraphCommand command = new AddNode(node);
                    return of(command);
                }
                return empty();
            }
        },

        ADD_EDGE(
                Pattern.compile("ADD EDGE (.*) (.*) (.*)")
        ) {
            @Override
            Optional<GraphCommand> produce(String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node1 = matcher.group(1);
                    final String node2 = matcher.group(2);
                    final String s = matcher.group(3);
                    final int weight = Integer.parseInt(s);
                    final GraphCommand command = new AddEdge(node1, node2, weight);
                    return of(command);
                }
                return empty();
            }
        };

        final Pattern pattern;

        Producer(final Pattern pattern) {
            this.pattern = pattern;
        }

        abstract Optional<GraphCommand> produce(final String text);

        Optional<GraphCommand> produceAndLog(final String text) {
            LOGGER.info("parsing " + name());
            final Optional<GraphCommand> command = produce(text);
            command.ifPresent(
                    c -> LOGGER.info("{}", c)
            );
            return command;
        }
    }

    static Optional<GraphCommand> parse(final String request) {

        final BiFunction<Optional<GraphCommand>, Producer, Optional<GraphCommand>> accumulator =
                (command, producer) ->
                        command.isPresent()
                                ? command
                                : producer.produceAndLog(request);

        final BinaryOperator<Optional<GraphCommand>> combiner =
                (command, ignore) -> command;

        return Stream.of(Producer.values()).reduce(empty(), accumulator, combiner);
    }
}

