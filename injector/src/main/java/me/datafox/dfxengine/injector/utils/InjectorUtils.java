package me.datafox.dfxengine.injector.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
     * @param string class signature to resolve
     * @return resolved superclasses and interfaces from the class signature
     */
    public static Stream<String> getSuperclasses(String string) {
        if(!string.contains("<")) {
            return Arrays.stream(string.split(" extends | implements ", 2)[1].split(" extends | implements ")).map(InjectorUtils::splitParameters).flatMap(List::stream);
        }
        List<String> superclasses = new ArrayList<>();
        int withinParams = 0;
        int start = 0;
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == '<') {
                withinParams++;
                continue;
            }
            if(string.charAt(i) == '>') {
                withinParams--;
                continue;
            }
            if(withinParams == 0) {
                if(string.startsWith(", ", i) || string.startsWith(" extends ", i) || string.startsWith( " implements ", i)) {
                    superclasses.add(string.substring(start, i));
                    if(string.startsWith(", ", i)) {
                        i++;
                    } else if(string.startsWith(" extends ", i)) {
                        i += 8;
                    } else {
                        i += 11;
                    }
                    start = i + 1;
                }
            }
        }
        superclasses.add(string.substring(start));
        if(superclasses.size() == 1) {
            return Stream.empty();
        } else {
            return superclasses.subList(1, superclasses.size()).stream();
        }
    }

    /**
     *
     * @param string signature from which keywords should be stripped
     * @return specified signature with keywords stripped
     */
    public static String stripKeywords(String string) {
        for(String str : new String[] {" public ", " abstract ", " interface "}) {
            while(string.contains(str)) {
                string = string.substring(0, string.indexOf(str)) + string.substring(string.indexOf(str) + str.length() - 1);
            }
        }
        return string;
    }
}
