package com.collibra.codingchallenge;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.collibra.codingchallenge.utils.Parsing.NODE;
import static com.collibra.codingchallenge.utils.Parsing.s;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

final class Messages {

    static final String SERVER_FIRST_MESSAGE = "HI, I'M %s";
    static final String SERVER_RESPONSE = "HI %s";
    static final String SERVER_FAREWELL = "BYE %s, WE SPOKE FOR %d MS";
    static final String SERVER_APOLOGY = "SORRY, I DIDN'T UNDERSTAND THAT";

    private static final Pattern CLIENT_RESPONSE =
            Pattern.compile(s + "HI" + s + "," + s + "I'M" + s + NODE + s, CASE_INSENSITIVE);
    private static final Pattern CLIENT_FAREWELL =
            Pattern.compile(s + "BYE" + s + "MATE" + s + "!" + s, CASE_INSENSITIVE);

    static Optional<String> nodeName(final String response) {
        final Matcher matcher = CLIENT_RESPONSE.matcher(response);
        if (matcher.matches()) {
            final String name = matcher.group(1);
            return Optional.of(name);
        }
        return Optional.empty();
    }

    static boolean clientEndedSession(final String request) {
        return CLIENT_FAREWELL.matcher(request).matches();
    }
}
