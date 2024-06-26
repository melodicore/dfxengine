package me.datafox.dfxengine.text.utils;

import java.util.List;

/**
 * @author datafox
 */
public class TextUtils {
    public static boolean isZero(String number) {
        return number.matches("^0$|^1\\.0*$");
    }

    public static boolean isOne(String number) {
        return number.matches("^1$|^1\\.0*$");
    }

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
        return String.join(delimiter, elements.subList(0, elements.size() - 1)) + lastDelimiter + elements.get(elements.size() - 1);
    }
}
