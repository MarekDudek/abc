package com.collibra.codingchallenge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

final class Messages {

    private static final Pattern CLIENT_HI_MESSAGE = Pattern.compile("HI, I'M (.+)", CASE_INSENSITIVE);

    static String clientName(final String message) {
        final Matcher matcher = CLIENT_HI_MESSAGE.matcher(message);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }
}
