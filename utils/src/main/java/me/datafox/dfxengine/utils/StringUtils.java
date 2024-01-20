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
     * @param str input {@link String}
     * @param target target to be replaced
     * @param replacement replacement {@link String}
     * @return modified {@link String}
     */
    public static String replaceLast(String str, String target, String replacement) {
        int i = str.lastIndexOf(target);
        if(i < 0) return str;
        return str.substring(0, i).concat(str.substring(i).replace(target, replacement));
    }

    /**
     * @param aClass class
     * @param <T> type of the class
     * @return {@link String} representation of the class in <i>ClassName.class</i> format
     */
    public static <T> String className(Class<T> aClass) {
        return aClass.getSimpleName() + ".class";
    }

    /**
     * @param constructor constructor
     * @param <T> type of the constructor
     * @return {@link String} representation of the constructor in <i>ClassName(Param1.class,Param2.class)</i> format
     */
    public static <T> String constructorName(Constructor<T> constructor) {
        return String.format("%s(%s)", constructor.getDeclaringClass().getSimpleName(),
                Arrays.stream(constructor.getParameterTypes())
                        .map(StringUtils::className)
                        .collect(Collectors.joining(",")));
    }
}
