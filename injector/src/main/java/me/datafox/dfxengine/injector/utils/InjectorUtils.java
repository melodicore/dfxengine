package me.datafox.dfxengine.injector.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public class InjectorUtils {
    public static List<String> splitParameters(String typeParameters) {
        List<String> list = new ArrayList<>();
        int counter = 0;
        int start = 0;
        int end = typeParameters.length();
        for(int i = 0; i < typeParameters.length(); i++) {
            if(counter == 0) {
                if(typeParameters.startsWith(", ", i)) {
                    list.add(typeParameters.substring(start, i));
                    i++;
                    start = i + 1;
                    continue;
                }
            }
            if(typeParameters.charAt(i) == '<') {
                counter++;
            } else if(typeParameters.charAt(i) == '>') {
                counter--;
                if(counter < 0) {
                    end = i;
                    break;
                }
            }
        }
        list.add(typeParameters.substring(start, end));
        return list;
    }

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

    public static String stripKeywords(String string) {
        for(String str : new String[] {" public ", " abstract "}) {
            while(string.contains(str)) {
                string = string.substring(0, string.indexOf(str)) + string.substring(string.indexOf(str) + str.length() - 1);
            }
        }
        return string;
    }
}
