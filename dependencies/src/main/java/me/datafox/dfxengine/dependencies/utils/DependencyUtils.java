package me.datafox.dfxengine.dependencies.utils;

import me.datafox.dfxengine.dependencies.Dependency;
import me.datafox.dfxengine.dependencies.Dependent;
import me.datafox.dfxengine.utils.ClassUtils;

/**
 * @author datafox
 */
public class DependencyUtils {
    public static boolean checkCyclicDependencies(Dependent parent, Dependency child) {
        if(!(parent instanceof Dependency && child instanceof Dependent)) {
            return true;
        }

        return checkCyclicDependenciesRecursive((Dependency) parent, (Dependent) child);
    }

    private static boolean checkCyclicDependenciesRecursive(Dependency original, Dependent current) {
        if(current.containsDependency(original)) {
            return false;
        }
        return current.dependencyStream()
                .flatMap(dependency ->
                        ClassUtils.filterInstanceAndCast(dependency, Dependent.class))
                .allMatch(dependent -> checkCyclicDependenciesRecursive(original, dependent));
    }
}
