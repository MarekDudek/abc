package com.collibra.codingchallenge.graphs;

import com.collibra.codingchallenge.commands.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class GraphCommandParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphCommandParser.class);


    public static Optional<GraphCommand> parse(final String request) {
        final BiFunction<Optional<GraphCommand>, Parser, Optional<GraphCommand>> first =
                (command, parser) ->
                        command.isPresent()
                                ? command
                                : parser.parseAndLog(request);
        return Stream.of(Parser.values()).reduce(Optional.empty(), first, CONST);
    }

    private static final BinaryOperator<Optional<GraphCommand>> CONST = (a, b) -> a;


    @AllArgsConstructor
    private enum Parser {

        ADD_NODE(
                Pattern.compile("ADD NODE (.*)")
        ) {
            Optional<GraphCommand> parse(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node = matcher.group(1);
                    final GraphCommand command = new AddNode(node);
                    return Optional.of(command);
                }
                return Optional.empty();
            }
        },

        ADD_EDGE(
                Pattern.compile("ADD EDGE (.*) (.*) (.*)")
        ) {
            Optional<GraphCommand> parse(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String from = matcher.group(1);
                    final String to = matcher.group(2);
                    final String str = matcher.group(3);
                    final int weight = Integer.parseInt(str);
                    final GraphCommand command = new AddEdge(from, to, weight);
                    return Optional.of(command);
                }
                return Optional.empty();
            }
        },

        REMOVE_NODE(
                Pattern.compile("REMOVE NODE (.*)")
        ) {
            Optional<GraphCommand> parse(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node = matcher.group(1);
                    final GraphCommand command = new RemoveNode(node);
                    return Optional.of(command);
                }
                return Optional.empty();
            }
        },

        REMOVE_EDGE(
                Pattern.compile("REMOVE EDGE (.*) (.*)")
        ) {
            Optional<GraphCommand> parse(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String from = matcher.group(1);
                    final String to = matcher.group(2);
                    final GraphCommand command = new RemoveEdge(from, to);
                    return Optional.of(command);
                }
                return Optional.empty();
            }
        },

        SHORTEST_PATH(
                Pattern.compile("SHORTEST PATH (.*) (.*)")
        ) {
            Optional<GraphCommand> parse(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String from = matcher.group(1);
                    final String to = matcher.group(2);
                    final GraphCommand command = new ShortestPath(from, to);
                    return Optional.of(command);
                }
                return Optional.empty();
            }
        },

        CLOSER_THAN(
                Pattern.compile("CLOSER THAN (.*) (.*)")
        ) {
            Optional<GraphCommand> parse(final String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String str = matcher.group(1);
                    final int weight = Integer.parseInt(str);
                    final String node = matcher.group(2);
                    final GraphCommand command = new CloserThan(weight, node);
                    return Optional.of(command);
                }
                return Optional.empty();
            }
        };

        final Pattern pattern;


        abstract Optional<GraphCommand> parse(String text);

        Optional<GraphCommand> parseAndLog(final String text) {
            LOGGER.info("parsing " + name());
            final Optional<GraphCommand> command = parse(text);
            command.ifPresent(
                    c -> LOGGER.info("{}", c)
            );
            return command;
        }
    }
}
