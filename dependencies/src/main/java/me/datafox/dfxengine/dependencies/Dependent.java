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

    boolean addDependencies(Collection<Dependency> dependencies);

    boolean removeDependency(Dependency dependency);

    boolean removeDependencies(Collection<Dependency> dependencies);

    boolean containsDependency(Dependency dependency);

    boolean containsDependencies(Collection<Dependency> dependencies);

    boolean containsDependencyRecursive(Dependency dependency);

    boolean containsDependenciesRecursive(Collection<Dependency> dependencies);

    Stream<Dependency> dependencyStream();

    Stream<Dependency> recursiveDependencyStream();
}
