package com.collibra.codingchallenge.parsing;

import com.collibra.codingchallenge.commands.*;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.collibra.codingchallenge.parsing.Regex.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

public final class GraphCommandParser {

    public static Optional<GraphCommand> parse(final String request) {
        return Stream.of(Parser.values()).flatMap(
                parser -> {
                    final Optional<GraphCommand> command = parser.parse(request);
                    return command.map(Stream::of).orElseGet(Stream::empty);
                }
        ).findFirst();
    }

    @AllArgsConstructor
    private enum Parser {

        ADD_NODE(
                Pattern.compile(s + "ADD" + s + "NODE" + s + NODE + s, CASE_INSENSITIVE)
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
                Pattern.compile(s + "ADD" + s + "EDGE" + s + NODE + s + NODE + s + WEIGHT + s, CASE_INSENSITIVE)
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
                Pattern.compile(s + "REMOVE" + s + "NODE" + s + NODE + s, CASE_INSENSITIVE)
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
                Pattern.compile(s + "REMOVE" + s + "EDGE" + s + NODE + s + NODE + s, CASE_INSENSITIVE)
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
                Pattern.compile(s + "SHORTEST" + s + "PATH" + s + NODE + s + NODE + s, CASE_INSENSITIVE)
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
                Pattern.compile(s + "CLOSER" + s + "THAN" + s + WEIGHT + s + NODE + s, CASE_INSENSITIVE)
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
    }
}
