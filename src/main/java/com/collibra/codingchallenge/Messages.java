package com.collibra.codingchallenge;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

final class Messages
{
    static String serverSessionHi(final UUID uuid)
    {
        return format("HI, I'M %s", uuid);
    }

    private static final Pattern CLIENT_HI_MESSAGE = Pattern.compile("HI, I'M (\\w+)", CASE_INSENSITIVE);

    static String parseClientHiMessage(final String message)
    {
        final Matcher matcher = CLIENT_HI_MESSAGE.matcher(message);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

    static String serverNameHi(final String name)
    {
        return format("HI %s", name);
    }

    private static final Pattern CLIENT_BYE_MESSAGE = Pattern.compile("BYE MATE!", CASE_INSENSITIVE);

    static boolean parseClientBye(final String message)
    {
        final Matcher matcher = CLIENT_BYE_MESSAGE.matcher(message);
        return matcher.matches();
    }

    static String serverBye(final String name, final long millis)
    {
        return format("BYE %s, WE SPOKE FOR %d MS", name, millis);
    }

    static String didNotUnderstand() {
        return "SORRY, I DIDN'T UNDERSTAND THAT";
    }
}
