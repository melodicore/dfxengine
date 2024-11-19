package me.datafox.dfxengine.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
     * Returns the name of a class.
     *
     * @param aClass class
     * @param <T> type of the class
     * @return {@link String} representation of the class in <i>ClassName.class</i> format
     */
    public static <T> String className(Class<T> aClass) {
        return aClass.getSimpleName() + ".class";
    }

    /**
     * Returns the name of a type.
     *
     * @param type type
     * @return {@link String} representation of the type in <i>ClassName.class</i> format
     */
    public static String typeName(Type type) {
        String str = type.getTypeName();
        int i = str.indexOf("<");
        if(i == -1) {
            return str.substring(str.lastIndexOf(".") + 1) + ".class";
        }
        int j = str.lastIndexOf(".", i);
        return str.substring(j + 1, i) + ".class";
    }

    /**
     * Returns the name of a constructor with its parameters.
     *
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

    /**
     * Capitalizes a {@link String}.
     *
     * @param str {@link String} to be capitalized
     * @return capitalized {@link String}
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() +
                str.substring(1);
    }

    /**
     * Joins the specified elements together using the specified separator and last separator.
     *
     * @param elements {@link CharSequence} elements to be joined
     * @param separator separator to be used
     * @param lastSeparator separator to be used before the last element, or {@code null} if the normal separator should
     * be used instead
     * @return {@link String} of the elements joined together
     */
    public static String joining(Collection<? extends CharSequence> elements, String separator, String lastSeparator) {
        if(elements.isEmpty()) {
            return "";
        } else if(elements.size() == 1) {
            return elements.iterator().next().toString();
        }

        String result = String.join(separator, elements);

        if(lastSeparator == null || separator.equals(lastSeparator)) {
            return result;
        }

        int lastLength = new ArrayList<>(elements).get(elements.size() - 1).length();

        return result.substring(0, result.length() - lastLength - separator.length()) +
                lastSeparator +
                result.substring(result.length() - lastLength);
    }
}
