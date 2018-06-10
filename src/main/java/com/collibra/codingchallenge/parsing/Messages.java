package com.collibra.codingchallenge.parsing;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.collibra.codingchallenge.parsing.Regex.NODE;
import static com.collibra.codingchallenge.parsing.Regex.s;
import static java.util.Comparator.naturalOrder;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.stream.Collectors.joining;

public final class Messages {

    public static final String SERVER_FIRST_MESSAGE = "HI, I'M %s";
    public static final String SERVER_RESPONSE = "HI %s";
    public static final String SERVER_FAREWELL = "BYE %s, WE SPOKE FOR %d MS";
    public static final String SERVER_NOT_SUPPORTED_COMMAND = "SORRY, I DIDN'T UNDERSTAND THAT";


    private static final Pattern CLIENT_RESPONSE =
            Pattern.compile(s + "HI" + s + "," + s + "I'M" + s + NODE + s, CASE_INSENSITIVE);
    private static final Pattern CLIENT_FAREWELL =
            Pattern.compile(s + "BYE" + s + "MATE" + s + "!" + s, CASE_INSENSITIVE);

    public static Optional<String> nodeName(final String response) {
        final Matcher matcher = CLIENT_RESPONSE.matcher(response);
        if (matcher.matches()) {
            final String name = matcher.group(1);
            return Optional.of(name);
        }
        return Optional.empty();
    }

    public static boolean clientEndedSession(final String request) {
        return CLIENT_FAREWELL.matcher(request).matches();
    }


    public static final String NODE_ADDED = "NODE ADDED";
    public static final String EDGE_ADDED = "EDGE ADDED";

    public static final String NODE_REMOVED = "NODE REMOVED";
    public static final String EDGE_REMOVED = "EDGE REMOVED";

    public static final String NODE_NOT_FOUND = "ERROR: NODE NOT FOUND";
    public static final String NODE_ALREADY_EXISTS = "ERROR: NODE ALREADY EXISTS";

    private static final String SHORTEST_PATH = "%d";

    public static String shortestPath(final Integer weight) {
        return String.format(SHORTEST_PATH, weight);
    }

    public static String closerThan(final List<String> names) {
        return names.stream().sorted(
                naturalOrder()
        ).collect(
                joining(",")
        );
    }
}
