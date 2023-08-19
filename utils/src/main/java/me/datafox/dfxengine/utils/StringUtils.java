package me.datafox.dfxengine.utils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utilities for manipulating strings
 *
 * @author datafox
 */
public class StringUtils {
    /**
     * Replaces the last occurrence of the target in a string.
     *
     * @param str input String
     * @param target target to be replaced
     * @param replacement replacement
     * @return modified String
     */
    public static String replaceLast(String str, String target, String replacement) {
        int i = str.lastIndexOf(target);
        if(i < 0) return str;
        return str.substring(0, i).concat(str.substring(i).replace(target, replacement));
    }

    /**
     * Returns the simple name of a class with a <b>.class</b> suffix
     *
     * @param aClass class
     * @return String representation of the class
     */
    public static <T> String className(Class<T> aClass) {
        return aClass.getSimpleName() + ".class";
    }

    /**
     * Returns a String representation of a constructor in <b>ClassName(Param1.class,Param2.class)</b> format
     * @param constructor constructor
     * @return String representation of the constructor
     */
    public static <T> String constructorName(Constructor<T> constructor) {
        return String.format("%s(%s)", constructor.getDeclaringClass().getSimpleName(),
                Arrays.stream(constructor.getParameterTypes())
                        .map(StringUtils::className)
                        .collect(Collectors.joining(",")));
    }
}
