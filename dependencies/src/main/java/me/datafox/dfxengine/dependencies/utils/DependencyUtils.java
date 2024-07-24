package me.datafox.dfxengine.dependencies.utils;

import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.utils.ClassUtils;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Utilities used internally in this module.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class DependencyUtils {
    public static boolean checkCyclicDependencies(Dependency parent, Dependent child) {
        if(!(parent instanceof Dependent && child instanceof Dependency)) {
            return false;
        }

        return containsDependencyRecursive((Dependent) parent, (Dependency) child);
    }

    public static boolean containsDependencyRecursive(Dependent dependency, Dependency current) {
        if(current.containsDependent(dependency)) {
            return true;
        }
        return current.dependentStream()
                .flatMap(DependencyUtils::flatMapDependencies)
                .anyMatch(Predicate.isEqual(dependency));
    }

    public static Stream<Dependent> flatMapDependencies(Dependent dependency) {
        return Stream.concat(Stream.of(dependency),
                ClassUtils.filterInstanceAndCast(dependency, Dependency.class)
                        .flatMap(Dependency::dependentStream));
    }
}
