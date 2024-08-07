package me.datafox.dfxengine.injector.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains various utilities used internally by this module.
 *
 * @author datafox
 */
public class InjectorUtils {
    /**
     * Splits a string by the delimiter "{@code , }", but only if the delimiter is not within angle brackets.
     *
     * @param parameters parameters to split
     * @return list of split parameters
     */
    public static List<String> splitParameters(String parameters) {
        List<String> list = new ArrayList<>();
        int counter = 0;
        int start = 0;
        int end = parameters.length();
        for(int i = 0; i < parameters.length(); i++) {
            if(counter == 0) {
                if(parameters.startsWith(", ", i)) {
                    list.add(parameters.substring(start, i));
                    i++;
                    start = i + 1;
                    continue;
                }
            }
            if(parameters.charAt(i) == '<') {
                counter++;
            } else if(parameters.charAt(i) == '>') {
                counter--;
                if(counter < 0) {
                    end = i;
                    break;
                }
            }
        }
        list.add(parameters.substring(start, end));
        return list;
    }

    /**
     * @param str string to be processed
     * @return specified string with everything between {@code <} and {@code >} removed
     */
    public static String removeTypes(String str) {
        if(!str.contains("<")) {
            return str;
        }
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '<') {
                counter++;
            } else if(str.charAt(i) == '>') {
                counter--;
            } else if(counter == 0) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * @param str string to be split
     * @param split split delimiter
     * @param max maximum amount of elements
     * @return specified string split by the specified delimiter, ignoring delimiters that appear between {@code <} and
     * {@code >}
     */
    public static String[] splitWithoutTypes(String str, String split, int max) {
        List<String> splits = new ArrayList<>(max);
        int counter = 0;
        int start = 0;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '<') {
                counter++;
            } else if(str.charAt(i) == '>') {
                counter--;
            } else if(str.startsWith(split, i) && counter == 0) {
                splits.add(str.substring(start, i));
                i += split.length();
                start = i;
                if(splits.size() == max - 1) {
                    break;
                }
            }
        }
        splits.add(str.substring(start));
        return splits.toArray(String[]::new);
    }

    public static String escapeCapture(String str) {
        if(!str.contains("$")) {
            return str;
        }
        return String.join("\\$", str.split("\\$"));
    }
}
