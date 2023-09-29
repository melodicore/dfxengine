package me.datafox.dfxengine.dependencies;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependency}, and all Dependencies should be added to the class implementing this interface
 * with {@link #addDependency(Dependency)} or {@link #addDependencies(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the
 * operation.
 *
 * @author datafox
 */
public interface Dependent {
    /**
     * @return all {@link Dependency Dependencies} that depend on this class
     */
    Collection<Dependency> getDependencies();

    /**
     * Calls {@link Dependency#invalidate()} on all {@link Dependency Dependencies} of this class.
     */
    void invalidateDependencies();

    /**
     * Register a {@link Dependency} that depends on this class. The method must check for cyclic dependencies and throw
     * {@link IllegalArgumentException} if one is detected.
     *
     * @param dependency {@link Dependency} that depends on this class
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    boolean addDependency(Dependency dependency);

    /**
     * Register {@link Dependency Dependencies} that depends on this class. The method must check for cyclic
     * dependencies and throw {@link IllegalArgumentException} if one is detected.
     *
     * @param dependencies {@link Dependency Dependencies} that depend on this class
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    boolean addDependencies(Collection<? extends Dependency> dependencies);

    /**
     * @param dependency {@link Dependency} to be removed
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     */
    boolean removeDependency(Dependency dependency);

    /**
     * @param dependencies {@link Dependency Dependencies} to be removed
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     */
    boolean removeDependencies(Collection<? extends Dependency> dependencies);

    /**
     * @param dependency {@link Dependency} to be checked for
     * @return {@code true} if the specified {@link Dependency} is registered to this class
     */
    boolean containsDependency(Dependency dependency);

    /**
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return {@code true} if all the specified {@link Dependency Dependencies} are registered to this class
     */
    boolean containsDependencies(Collection<? extends Dependency> dependencies);

    /**
     * Checks if the specified {@link Dependency} is registered to this class or any of its Dependencies that also
     * implement Dependent, recursively. In practice any Dependency that depends on this class, directly or indirectly,
     * would cause this method to return {@code true}.
     *
     * @param dependency {@link Dependency} to be checked for
     * @return {@code true} if the specified {@link Dependency} is registered to this class or any of its Dependencies
     * that also implement Dependent, recursively
     */
    boolean containsDependencyRecursive(Dependency dependency);

    /**
     * Checks if all the specified {@link Dependency Dependencies} are registered to this class or any of its
     * Dependencies that also implement Dependent, recursively. In practice any collection of Dependencies that depend
     * on this class, directly or indirectly, would cause this method to return {@code true}.
     *
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return {@code true} if the specified {@link Dependency Dependencies} are registered to this class or any of its
     * Dependencies that also implement Dependent, recursively
     */
    boolean containsDependenciesRecursive(Collection<? extends Dependency> dependencies);

    /**
     * @return {@link Stream} of all {@link Dependency Dependencies} that are registered to this class
     */
    Stream<Dependency> dependencyStream();

    /**
     * @return {@link Stream} of all {@link Dependency Dependencies} that are registered to this class or any of its
     * Dependencies that also implement Dependent, recursively
     */
    Stream<Dependency> recursiveDependencyStream();
}
