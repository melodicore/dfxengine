package me.datafox.dfxengine.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilities for classes and reflection.
 *
 * @author datafox
 */
public class ClassUtils {
    /**
     * Returns a {@link Stream} of the class and all superclasses and superinterfaces of the class.
     *
     * @param aClass class
     * @param <T> type of the class
     * @return {@link Stream} of the class and all superclasses and superinterfaces of the class
     */
    public static <T> Stream<Class<? super T>> getSuperclassesFor(Class<? super T> aClass) {
        return Stream.concat(
                Stream.of(aClass),
                getSuperclassesRecursive(aClass)
        );
    }

    /**
     * Returns a list of all declared constructors of the class with the annotation.
     *
     * @param aClass class
     * @param annotation annotation
     * @param <T> type of the class
     * @return List of all declared constructors of the class with the annotation
     */
    @SuppressWarnings("unchecked")
    public static <T> List<Constructor<T>> getConstructorsWithAnnotation(Class<T> aClass,
                                                                         Class<? extends Annotation> annotation) {
        return Arrays
                .stream(aClass.getDeclaredConstructors())
                .filter(constructor ->
                        constructor.isAnnotationPresent(annotation))
                .map(constructor -> (Constructor<T>) constructor)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all declared fields of the class with the annotation.
     *
     * @param aClass class
     * @param annotation annotation
     * @param <T> type of the class
     * @return List of all declared fields of the class with the annotation
     */
    public static <T> List<Field> getFieldsWithAnnotation(Class<T> aClass, Class<? extends Annotation> annotation) {
        return Arrays
                .stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all declared methods of the class with the annotation.
     *
     * @param aClass class
     * @param annotation annotation
     * @param <T> type of the class
     * @return List of all declared methods of the class with the annotation
     */
    public static <T> List<Method> getMethodsWithAnnotation(Class<T> aClass, Class<? extends Annotation> annotation) {
        return Arrays
                .stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    /**
     * Returns an {@link Optional} containing the first annotation in the array matching the type, or empty if no
     * matches are found.
     *
     * @param arr array of annotations
     * @param annotationType annotation type
     * @param <T> type of the annotation
     * @return {@link Optional} containing the first annotation in the array matching the type, or empty if no matches
     * are found
     */
    public static <T extends Annotation> Optional<T> getAnnotationFromArray(Annotation[] arr, Class<T> annotationType) {
        return Arrays
                .stream(arr)
                .filter(annotationType::isInstance)
                .map(annotationType::cast)
                .findFirst();
    }

    /**
     * Returns a {@link Stream} of the object cast to the specified {@link Class}, or an empty stream if the object
     * cannot be cast to the specified class.
     *
     * @param input object to be checked
     * @param aClass {@link Class}
     * @param <T> type of the input
     * @param <C> type to be cast to
     * @return {@link Stream} of the object cast to the specified {@link Class}, or an empty stream if the object cannot
     * be cast to the specified class
     */
    public static <T,C> Stream<C> filterInstanceAndCast(T input, Class<C> aClass) {
        return aClass.isInstance(input) ? Stream.of(aClass.cast(input)) : Stream.empty();
    }

    /**
     * Returns a {@link Function} that returns a {@link Stream} of the object cast to the specified {@link Class}, or an
     * empty stream if the object cannot be cast to the specified class.
     *
     * @param aClass {@link Class}
     * @param <T> type of the input
     * @param <C> type to be cast to
     * @return {@link Function} that returns a {@link Stream} of the object cast to the specified {@link Class}, or an
     * empty stream if the object cannot be cast to the specified class
     */
    public static <T,C> Function<T,Stream<C>> filterInstanceAndCast(Class<C> aClass) {
        return input -> filterInstanceAndCast(input, aClass);
    }

    @SuppressWarnings("unchecked")
    private static <T> Stream<Class<? super T>> getSuperclassesRecursive(Class<? super T> aClass) {
        Set<Class<? super T>> classes = new HashSet<>();

        if(aClass.getSuperclass() != null) {
            classes.add(aClass.getSuperclass());
        }

        for(Class<?> anInterface : aClass.getInterfaces()) {
            classes.add((Class<? super T>) anInterface);
        }

        return Stream.concat(
                classes.stream(),
                classes.stream().flatMap(ClassUtils::getSuperclassesRecursive)
        );
    }
}
