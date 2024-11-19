package me.datafox.dfxengine.dependencies;

import me.datafox.dfxengine.dependencies.utils.DependencyStrings;
import me.datafox.dfxengine.dependencies.utils.DependencyUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p>
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependent}, and all dependents should be added to the class implementing this interface with
 * {@link #addDependent(Dependent)} or {@link #addDependents(Collection)}. The implementation of these methods must
 * check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the operation.
 * </p>
 * <p>
 * The cyclic dependency detection in this class will only detect dependent dependencies that implement both
 * {@link Dependency} and {@link Dependent}. It is recommended to extend {@link DependencyDependent} where possible for
 * all cases where both interfaces would be implemented.
 * </p>
 *
 * @author datafox
 */
public abstract class AbstractDependency implements Dependency {
    /**
     * {@link Logger} for this dependency.
     */
    protected final Logger logger;

    /**
     * Set of {@link Dependent Dependents} for this dependency.
     */
    protected final Set<Dependent> dependents;

    /**
     * Protected constructor for {@link AbstractDependency}.
     *
     * @param logger {@link Logger} for this dependent
     */
    protected AbstractDependency(Logger logger) {
        this.logger = logger;
        dependents = new HashSet<>();
    }

    /**
     * Returns all {@link Dependent Dependents} that depend on this class.
     *
     * @return all {@link Dependent Dependents} that depend on this class
     */
    @Override
    public Collection<Dependent> getDependents() {
        return Collections.unmodifiableSet(dependents);
    }

    /**
     * Calls {@link Dependent#invalidate()} on all {@link Dependent Dependents} of this class.
     */
    @Override
    public void invalidateDependents() {
        dependents.forEach(Dependent::invalidate);
    }

    /**
     * Register a {@link Dependent} that depends on this class. The method must check for cyclic dependencies and throw
     * {@link IllegalArgumentException} if one is detected.
     *
     * @param dependent {@link Dependent} that depends on this class
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependent(Dependent dependent) {
        if(DependencyUtils.checkCyclicDependencies(this, dependent)) {
            throw LogUtils.logExceptionAndGet(logger,
                DependencyStrings.cyclicDetected(dependent, this),
                IllegalArgumentException::new);
        }

        return dependents.add(dependent);
    }

    /**
     * Register {@link Dependent Dependents} that depends on this class. The method must check for cyclic dependencies
     * and throw {@link IllegalArgumentException} if one is detected.
     *
     * @param dependents {@link Dependent Dependents} that depend on this class
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependents(Collection<? extends Dependent> dependents) {
        for(Dependent dependent : dependents) {
            if(DependencyUtils.checkCyclicDependencies(this, dependent)) {
                throw LogUtils.logExceptionAndGet(logger,
                        DependencyStrings.cyclicDetected(dependent, this),
                        IllegalArgumentException::new);
            }
        }

        return this.dependents.addAll(dependents);
    }

    /**
     * Removes a {@link Dependent} from this dependency.
     *
     * @param dependent {@link Dependent} to be removed
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     */
    @Override
    public boolean removeDependent(Dependent dependent) {
        return dependents.remove(dependent);
    }

    /**
     * Removes {@link Dependent Dependents} from this dependency.
     *
     * @param dependents {@link Dependent Dependents} to be removed
     * @return {@code true} if the registered {@link Dependent Dependents} changed as a result of this operation
     */
    @Override
    public boolean removeDependents(Collection<? extends Dependent> dependents) {
        return this.dependents.removeAll(dependents);
    }

    /**
     * Checks if a {@link Dependent} is present in this dependency.
     *
     * @param dependent {@link Dependent} to be checked for
     * @return {@code true} if the specified {@link Dependent} is registered to this class
     */
    @Override
    public boolean containsDependent(Dependent dependent) {
        return dependents.contains(dependent);
    }

    /**
     * Checks if {@link Dependent Dependents} are present in this dependency.
     *
     * @param dependents {@link Dependent Dependents} to be checked for
     * @return {@code true} if all the specified {@link Dependent Dependents} are registered to this class
     */
    @Override
    public boolean containsDependents(Collection<? extends Dependent> dependents) {
        return this.dependents.containsAll(dependents);
    }

    /**
     * Checks if the specified {@link Dependent} is registered to this class or any of its dependents that also
     * implement dependent, recursively. In practice any dependent that depends on this class, directly or indirectly,
     * would cause this method to return {@code true}.
     *
     * @param dependent {@link Dependent} to be checked for
     * @return {@code true} if the specified {@link Dependent} is registered to this class or any of its dependents that
     * also implement dependent, recursively
     */
    @Override
    public boolean containsDependentRecursive(Dependent dependent) {
        return DependencyUtils.containsDependencyRecursive(dependent, this);
    }

    /**
     * Checks if all the specified {@link Dependent Dependents} are registered to this class or any of its dependencies
     * that also implement dependent, recursively. In practice any collection of dependencies that depend on this class,
     * directly or indirectly, would cause this method to return {@code true}.
     *
     * @param dependents {@link Dependent Dependencies} to be checked for
     * @return {@code true} if the specified {@link Dependent Dependencies} are registered to this class or any of its
     * dependencies that also implement dependent, recursively
     */
    @Override
    public boolean containsDependentsRecursive(Collection<? extends Dependent> dependents) {
        return dependents.stream().allMatch(dependency ->
                DependencyUtils.containsDependencyRecursive(dependency, this));
    }

    /**
     * Returns a {@link Stream} of all {@link Dependent Dependents} that are registered to this class.
     *
     * @return {@link Stream} of all {@link Dependent Dependents} that are registered to this class
     */
    @Override
    public Stream<Dependent> dependentStream() {
        return dependents.stream();
    }

    /**
     * Returns a {@link Stream} of all {@link Dependent Dependents} that are registered to this class or any of its
     * dependents that also implement dependency, recursively.
     *
     * @return {@link Stream} of all {@link Dependent Dependents} that are registered to this class or any of its
     * dependents that also implement dependency, recursively
     */
    @Override
    public Stream<Dependent> recursiveDependentStream() {
        return dependents.stream().flatMap(DependencyUtils::flatMapDependencies);
    }
}
