package me.datafox.dfxengine.dependencies;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependent}, and all dependents should be added to the class implementing this interface
 * with {@link #addDependent(Dependent)} or {@link #addDependents(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the
 * operation.
 *
 * @author datafox
 */
public interface Dependency {
    /**
     * Returns all {@link Dependent Dependents} that depend on this class.
     *
     * @return all {@link Dependent Dependents} that depend on this class
     */
    Collection<Dependent> getDependents();

    /**
     * Calls {@link Dependent#invalidate()} on all {@link Dependent Dependents} of this class.
     */
    void invalidateDependents();

    /**
     * Register a {@link Dependent} that depends on this class. The method must check for cyclic dependencies and throw
     * {@link IllegalArgumentException} if one is detected.
     *
     * @param dependent {@link Dependent} that depends on this class
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    boolean addDependent(Dependent dependent);

    /**
     * Register {@link Dependent Dependents} that depends on this class. The method must check for cyclic dependencies
     * and throw {@link IllegalArgumentException} if one is detected.
     *
     * @param dependents {@link Dependent Dependents} that depend on this class
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    boolean addDependents(Collection<? extends Dependent> dependents);

    /**
     * Removes a {@link Dependent} from this dependency.
     *
     * @param dependent {@link Dependent} to be removed
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     */
    boolean removeDependent(Dependent dependent);

    /**
     * Removes {@link Dependent Dependents} from this dependency.
     *
     * @param dependents {@link Dependent Dependents} to be removed
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     */
    boolean removeDependents(Collection<? extends Dependent> dependents);

    /**
     * Checks if a {@link Dependent} is present in this dependency.
     *
     * @param dependent {@link Dependent} to be checked for
     * @return {@code true} if the specified {@link Dependent} is registered to this class
     */
    boolean containsDependent(Dependent dependent);

    /**
     * Checks if {@link Dependent Dependents} are present in this dependency.
     *
     * @param dependents {@link Dependent Dependents} to be checked for
     * @return {@code true} if all the specified {@link Dependent Dependents} are registered to this class
     */
    boolean containsDependents(Collection<? extends Dependent> dependents);

    /**
     * Checks if the specified {@link Dependent} is registered to this class or any of its dependents that also
     * implement dependent, recursively. In practice any dependent that depends on this class, directly or indirectly,
     * would cause this method to return {@code true}.
     *
     * @param dependent {@link Dependent} to be checked for
     * @return {@code true} if the specified {@link Dependent} is registered to this class or any of its dependents that
     * also implement dependent, recursively
     */
    boolean containsDependentRecursive(Dependent dependent);

    /**
     * Checks if all the specified {@link Dependent Dependents} are registered to this class or any of its dependencies
     * that also implement dependent, recursively. In practice any collection of dependencies that depend on this class,
     * directly or indirectly, would cause this method to return {@code true}.
     *
     * @param dependents {@link Dependent Dependencies} to be checked for
     * @return {@code true} if the specified {@link Dependent Dependencies} are registered to this class or any of its
     * dependencies that also implement dependent, recursively
     */
    boolean containsDependentsRecursive(Collection<? extends Dependent> dependents);

    /**
     * Returns a {@link Stream} of all {@link Dependent Dependents} that are registered to this class.
     *
     * @return {@link Stream} of all {@link Dependent Dependents} that are registered to this class
     */
    Stream<Dependent> dependentStream();

    /**
     * Returns a {@link Stream} of all {@link Dependent Dependents} that are registered to this class or any of its
     * dependents that also implement dependency, recursively.
     *
     * @return {@link Stream} of all {@link Dependent Dependents} that are registered to this class or any of its
     * dependents that also implement dependency, recursively
     */
    Stream<Dependent> recursiveDependentStream();
}
