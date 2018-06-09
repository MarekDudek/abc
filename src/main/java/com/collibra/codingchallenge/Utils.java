package com.collibra.codingchallenge;

public final class Utils {

    public static <R> R error(final String message) {
        throw new RuntimeException(message);
    }
}
