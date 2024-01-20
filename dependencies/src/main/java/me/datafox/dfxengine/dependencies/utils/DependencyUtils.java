package me.datafox.dfxengine.dependencies.utils;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;
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
    public static boolean checkCyclicDependencies(Dependent parent, Dependency child) {
        if(!(parent instanceof Dependency && child instanceof Dependent)) {
            return false;
        }

        return containsDependencyRecursive((Dependency) parent, (Dependent) child);
    }

    public static boolean containsDependencyRecursive(Dependency dependency, Dependent current) {
        if(current.containsDependency(dependency)) {
            return true;
        }
        return current.dependencyStream()
                .flatMap(DependencyUtils::flatMapDependencies)
                .anyMatch(Predicate.isEqual(dependency));
    }

    public static Stream<Dependency> flatMapDependencies(Dependency dependency) {
        return Stream.concat(Stream.of(dependency),
                ClassUtils.filterInstanceAndCast(dependency, Dependent.class)
                        .flatMap(Dependent::dependencyStream));
    }
}
