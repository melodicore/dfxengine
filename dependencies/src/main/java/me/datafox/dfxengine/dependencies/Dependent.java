package me.datafox.dfxengine.dependencies;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Dependent {
    Collection<Dependency> getDependencies();

    void invalidateDependencies();

    boolean addDependency(Dependency dependency);

    boolean removeDependency(Dependency dependency);

    boolean containsDependency(Dependency dependency);

    boolean containsDependencyRecursive(Dependency dependency);

    Stream<Dependency> dependencyStream();

    Stream<Dependency> recursiveDependencyStream();
}
