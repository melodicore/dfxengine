package me.datafox.dfxengine.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilities for classes and reflection.
 *
 * @author datafox
 */
public class ClassUtils {
    /**
     * @param aClass class
     * @return Stream of the class and all superclasses and superinterfaces of the class
     */
    public static <T> Stream<Class<? super T>> getSuperclassesFor(Class<? super T> aClass) {
        return Stream.concat(
                Stream.of(aClass),
                getSuperclassesRecursive(aClass)
        );
    }

    /**
     * @param aClass class
     * @param annotation annotation
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
     * @param aClass class
     * @param annotation annotation
     * @return List of all declared fields of the class with the annotation
     */
    public static <T> List<Field> getFieldsWithAnnotation(Class<T> aClass, Class<? extends Annotation> annotation) {
        return Arrays
                .stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    /**
     * @param aClass class
     * @param annotation annotation
     * @return List of all declared methods of the class with the annotation
     */
    public static <T> List<Method> getMethodsWithAnnotation(Class<T> aClass, Class<? extends Annotation> annotation) {
        return Arrays
                .stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    /**
     * @param arr array of annotations
     * @param annotationType annotation type
     * @return Optional containing the first annotation in the array matching the type, or empty if no matches are found
     */
    public static <T extends Annotation> Optional<T> getAnnotationFromArray(Annotation[] arr, Class<T> annotationType) {
        return Arrays
                .stream(arr)
                .filter(annotationType::isInstance)
                .map(annotationType::cast)
                .findFirst();
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
