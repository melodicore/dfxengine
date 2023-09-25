package me.datafox.dfxengine.dependencies;

import me.datafox.dfxengine.dependencies.utils.DependencyStrings;
import me.datafox.dfxengine.dependencies.utils.DependencyUtils;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * <p>
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependency}, and all Dependencies should be added to the class implementing this interface
 * with {@link #addDependency(Dependency)} or {@link #addDependencies(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the
 * operation.
 * </p>
 * <p>
 * The cyclic dependency detection in this class will only detect dependent dependencies that implement both
 * {@link Dependent} and {@link Dependency}. It is recommended to extend {@link DependencyDependent} where possible for
 * all cases where both interfaces would be implemented.
 * </p>
 *
 * @author datafox
 */
public abstract class AbstractDependent implements Dependent {
    /**
     * {@link Logger} for this dependent
     */
    protected final Logger logger;

    /**
     * Set of {@link Dependency Dependencies} for this dependent
     */
    protected final Set<Dependency> dependencies;

    /**
     * @param logger {@link Logger} for this dependent
     */
    protected AbstractDependent(Logger logger) {
        this.logger = logger;
        dependencies = new HashSet<>();
    }

    /**
     * @param logger {@link Logger} for this dependent
     * @param set {@link Supplier} for a {@link Set} containing {@link Dependency Dependencies} to be used as the
     * backing set
     */
    protected AbstractDependent(Logger logger, Supplier<Set<Dependency>> set) {
        this.logger = logger;
        dependencies = set.get();
    }

    /**
     * @return all {@link Dependency Dependencies} that depend on this class
     */
    @Override
    public Collection<Dependency> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    /**
     * Calls {@link Dependency#invalidate()} on all {@link Dependency Dependencies} of this class.
     */
    @Override
    public void invalidateDependencies() {
        dependencies.forEach(Dependency::invalidate);
    }

    /**
     * Register a {@link Dependency} that depends on this class. The method must check for cyclic dependencies and throw
     * {@link IllegalArgumentException} if one is detected.
     *
     * @param dependency {@link Dependency} that depends on this class
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependency(Dependency dependency) {
        if(DependencyUtils.checkCyclicDependencies(this, dependency)) {
            throw LogUtils.logExceptionAndGet(logger,
                DependencyStrings.cyclicDetected(dependency, this),
                IllegalArgumentException::new);
        }

        return dependencies.add(dependency);
    }

    /**
     * Register {@link Dependency Dependencies} that depends on this class. The method must check for cyclic
     * dependencies and throw {@link IllegalArgumentException} if one is detected.
     *
     * @param dependencies {@link Dependency Dependencies} that depend on this class
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     *
     * @throws IllegalArgumentException if this operation would cause a cyclic dependency
     */
    @Override
    public boolean addDependencies(Collection<Dependency> dependencies) {
        for(Dependency dependency : dependencies) {
            if(DependencyUtils.checkCyclicDependencies(this, dependency)) {
                throw LogUtils.logExceptionAndGet(logger,
                        DependencyStrings.cyclicDetected(dependency, this),
                        IllegalArgumentException::new);
            }
        }

        return this.dependencies.addAll(dependencies);
    }

    /**
     * @param dependency {@link Dependency} to be removed
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     */
    @Override
    public boolean removeDependency(Dependency dependency) {
        return dependencies.remove(dependency);
    }

    /**
     * @param dependencies {@link Dependency Dependencies} to be removed
     * @return {@code true} if the registered {@link Dependency Dependencies} changed as a result of this operation
     */
    @Override
    public boolean removeDependencies(Collection<Dependency> dependencies) {
        return this.dependencies.removeAll(dependencies);
    }

    /**
     * @param dependency {@link Dependency} to be checked for
     * @return {@code true} if the specified {@link Dependency} is registered to this class
     */
    @Override
    public boolean containsDependency(Dependency dependency) {
        return dependencies.contains(dependency);
    }

    /**
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return {@code true} if all the specified {@link Dependency Dependencies} are registered to this class
     */
    @Override
    public boolean containsDependencies(Collection<Dependency> dependencies) {
        return this.dependencies.containsAll(dependencies);
    }

    /**
     * Checks if the specified {@link Dependency} is registered to this class or any of its Dependencies that also
     * implement {@link Dependent}, recursively. In practice any Dependency that depends on this class, directly or
     * indirectly, would cause this method to return {@code true}.
     *
     * @param dependency {@link Dependency} to be checked for
     * @return {@code true} if the specified {@link Dependency} is registered to this class or any of its Dependencies
     * that also implement {@link Dependent}, recursively
     */
    @Override
    public boolean containsDependencyRecursive(Dependency dependency) {
        return DependencyUtils.containsDependencyRecursive(dependency, this);
    }

    /**
     * Checks if all the specified {@link Dependency Dependencies} are registered to this class or any of its
     * Dependencies that also implement {@link Dependent}, recursively. In practice any collection of Dependencies that
     * depend on this class, directly or indirectly, would cause this method to return {@code true}.
     *
     * @param dependencies {@link Dependency Dependencies} to be checked for
     * @return {@code true} if the specified {@link Dependency Dependencies} are registered to this class or any of its
     * Dependencies that also implement {@link Dependent}, recursively
     */
    @Override
    public boolean containsDependenciesRecursive(Collection<Dependency> dependencies) {
        return dependencies.stream().allMatch(dependency ->
                DependencyUtils.containsDependencyRecursive(dependency, this));
    }

    /**
     * @return {@link Stream} of all {@link Dependency Dependencies} that are registered to this class
     */
    @Override
    public Stream<Dependency> dependencyStream() {
        return dependencies.stream();
    }

    /**
     * @return {@link Stream} of all {@link Dependency Dependencies} that are registered to this class or any of its
     * Dependencies that also implement {@link Dependent}, recursively
     */
    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return dependencies.stream().flatMap(DependencyUtils::flatMapDependencies);
    }
}
