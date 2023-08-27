package me.datafox.dfxengine.dependencies;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Dependent {
    Collection<Dependency> getDependencies();

    boolean addDependency(Dependency dependency);

    boolean removeDependency(Dependency dependency);

    boolean containsDependency(Dependency dependency);

    Stream<Dependency> dependencyStream();
}
