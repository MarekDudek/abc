package com.collibra.codingchallenge.utils;

public final class Utils {

    public static <R> R error(final String message) {
        throw new RuntimeException(message);
    }
}
