package me.datafox.dfxengine.text.utils;

import java.util.List;

/**
 * Utilities for this module
 *
 * @author datafox
 */
public class TextUtils {
    /**
     * The number is considered to be zero if it is {@code 0}, or {@code 0.} followed by zero or more zeroes.
     *
     * @param number number formatted as a {@link String}
     * @return {@code true} if the number is zero
     */
    public static boolean isZero(String number) {
        return number.matches("0|0\\.0*");
    }

    /**
     * The number is considered to be one if it is {@code 1}, or {@code 1.} followed by zero or more zeroes.
     *
     * @param number number formatted as a {@link String}
     * @return {@code true} if the number is one
     */
    public static boolean isOne(String number) {
        return number.matches("1|1\\.0*");
    }

    /**
     * Joins a {@link List} of {@link String Strings} together using a delimiter and a last delimiter. For an empty
     * list, this method returns an empty string. For a list with one element, that element is returned. For a list of
     * two elements, the returned string is {@code element1 + lastDelimiter + element2}. For all other lists, the normal
     * delimiter is used between all other elements, but the last delimiter is used between the second to last and the
     * last elements. The most common delimiters are {@code ", "} and {@code " and "}.
     *
     * @param delimiter delimiter to be used
     * @param lastDelimiter delimiter to be used between the second to last and last elements
     * @param elements {@link List} of {@link String Strings} to be joined
     * @return joined {@link String}
     */
    public static String join(String delimiter, String lastDelimiter, List<String> elements) {
        if(delimiter.equals(lastDelimiter)) {
            return String.join(delimiter, elements);
        }
        if(elements.isEmpty()) {
            return "";
        }
        if(elements.size() == 1) {
            return elements.get(0);
        }
        if(elements.size() == 2) {
            return elements.get(0) + lastDelimiter + elements.get(1);
        }
        return String.join(delimiter,
                elements.subList(0, elements.size() - 1)) +
                lastDelimiter + elements.get(elements.size() - 1);
    }
}
