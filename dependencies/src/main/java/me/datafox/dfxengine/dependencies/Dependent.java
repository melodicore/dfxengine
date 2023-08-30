package me.datafox.dfxengine.dependencies;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependency}, and all Dependencies should be added to the class implementing this interface
 * with {@link #addDependency(Dependency)} or {@link #addDependencies(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the operation.
 *
 * @author datafox
 */
public interface Dependent {
    /**
     * @return all {@link Dependency Dependencies} that depend on this class
     */
    Collection<Dependency> getDependencies();

    /**
     * Call {@link Dependency#invalidate()} on all Dependencies of this class.
     */
    void invalidateDependencies();

    /**
     * Register a {@link Dependency} that depends on this class. The method must check for cyclic dependencies and throw
     * {@link IllegalArgumentException} if one is detected.
     *
     * @param dependency Dependency that depends on this class
     * @return true if the registered Dependencies changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    boolean addDependency(Dependency dependency);

    /**
     * Register {@link Dependency Dependencies} that depends on this class. The method must check for cyclic
     * dependencies and throw {@link IllegalArgumentException} if one is detected.
     *
     * @param dependencies Dependencies that depend on this class
     * @return true if the registered Dependencies changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    boolean addDependencies(Collection<Dependency> dependencies);

    /**
     * @param dependency {@link Dependency} to be removed
     * @return true if the registered Dependencies changed as a result of this operation
     */
    boolean removeDependency(Dependency dependency);

    /**
     * @param dependencies {@link Dependency Dependencies} to be removed
     * @return true if the registered Dependencies changed as a result of this operation
     */
    boolean removeDependencies(Collection<Dependency> dependencies);

    /**
     * @param dependency {@link Dependency} to be checked for
     * @return true if the specified Dependency is registered to this class
     */
    boolean containsDependency(Dependency dependency);

    /**
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return true if all the specified Dependencies are registered to this class
     */
    boolean containsDependencies(Collection<Dependency> dependencies);

    /**
     * Checks if the specified {@link Dependency} is registered to this class or any of its Dependencies that also
     * implement Dependent, recursively. In practice any Dependency that depends on this class, directly or indirectly,
     * would cause this method to return true.
     *
     * @param dependency Dependency to be checked for
     * @return true if the specified Dependency is registered to this class or any of its Dependencies that also
     * implement Dependent, recursively
     */
    boolean containsDependencyRecursive(Dependency dependency);

    /**
     * Checks if all the specified {@link Dependency Dependencies} are registered to this class or any of its
     * Dependencies that also implement Dependent, recursively. In practice any collection of Dependencies that depend
     * on this class, directly or indirectly, would cause this method to return true.
     *
     * @param dependencies Dependencies to be checked for
     * @return true if the specified Dependencies are registered to this class or any of its Dependencies that also
     * implement Dependent, recursively
     */
    boolean containsDependenciesRecursive(Collection<Dependency> dependencies);

    /**
     * @return Stream of all {@link Dependency Dependencies} that are registered to this class
     */
    Stream<Dependency> dependencyStream();

    /**
     * @return Stream of all {@link Dependency Dependencies} that are registered to this class or any of its
     * Dependencies that also implement Dependent, recursively
     */
    Stream<Dependency> recursiveDependencyStream();
}
