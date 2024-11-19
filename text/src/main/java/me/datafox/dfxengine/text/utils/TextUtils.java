package me.datafox.dfxengine.text.utils;

/**
 * Utilities for this module
 *
 * @author datafox
 */
public class TextUtils {
    /**
     * Checks if the specified {@link String} represents the number zero. The number is considered to be zero if it is
     * {@code 0}, or {@code 0.} followed by zero or more zeroes.
     *
     * @param number number formatted as a {@link String}
     * @return {@code true} if the number is zero
     */
    public static boolean isZero(String number) {
        return number.matches("0|0\\.0*");
    }

    /**
     * Checks if the specified {@link String} represents the number one. The number is considered to be one if it is
     * {@code 1}, or {@code 1.} followed by zero or more zeroes.
     *
     * @param number number formatted as a {@link String}
     * @return {@code true} if the number is one
     */
    public static boolean isOne(String number) {
        return number.matches("1|1\\.0*");
    }
}
