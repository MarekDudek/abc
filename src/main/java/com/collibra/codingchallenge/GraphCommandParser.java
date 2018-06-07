package com.collibra.codingchallenge;

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

import static java.util.Optional.empty;
import static java.util.Optional.of;

final class GraphCommandParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphCommandParser.class);


    static Optional<GraphCommand> parse(final String request) {
        final BiFunction<Optional<GraphCommand>, Parser, Optional<GraphCommand>> first =
                (command, parser) ->
                        command.isPresent()
                                ? command
                                : parser.parseAndLog(request);
        return Stream.of(Parser.values()).reduce(empty(), first, CONST);
    }

    private static final BinaryOperator<Optional<GraphCommand>> CONST = (a, b) -> a;


    @AllArgsConstructor
    private enum Parser {

        ADD_NODE(
                Pattern.compile("ADD NODE (.*)")
        ) {
            @Override
            Optional<GraphCommand> parse(final String text) {
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
            Optional<GraphCommand> parse(String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node1 = matcher.group(1);
                    final String node2 = matcher.group(2);
                    final String str = matcher.group(3);
                    final int weight = Integer.parseInt(str);
                    final GraphCommand command = new AddEdge(node1, node2, weight);
                    return of(command);
                }
                return empty();
            }
        },

        REMOVE_NODE(
                Pattern.compile("REMOVE NODE (.*)")
        ) {
            @Override
            Optional<GraphCommand> parse(String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node = matcher.group(1);
                    final GraphCommand command = new RemoveNode(node);
                    return of(command);
                }
                return empty();
            }
        },

        REMOVE_EDGE(
                Pattern.compile("REMOVE EDGE (.*) (.*)")
        ) {
            @Override
            Optional<GraphCommand> parse(String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node1 = matcher.group(1);
                    final String node2 = matcher.group(2);
                    final GraphCommand command = new RemoveEdge(node1, node2);
                    return of(command);
                }
                return empty();
            }
        },

        SHORTEST_PATH(
                Pattern.compile("SHORTEST PATH (.*) (.*)")
        ) {
            @Override
            Optional<GraphCommand> parse(String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String node1 = matcher.group(1);
                    final String node2 = matcher.group(2);
                    final GraphCommand command = new ShortestPath(node1, node2);
                    return of(command);
                }
                return empty();
            }
        },

        CLOSER_THAN(
                Pattern.compile("CLOSER THAN (.*) (.*)")
        ) {
            @Override
            Optional<GraphCommand> parse(String text) {
                final Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    final String str = matcher.group(1);
                    final int weight = Integer.parseInt(str);
                    final String node = matcher.group(2);
                    final GraphCommand command = new CloserThan(weight, node);
                    return of(command);
                }
                return empty();
            }
        };

        final Pattern pattern;


        abstract Optional<GraphCommand> parse(final String text);

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

