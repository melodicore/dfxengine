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
 * should implement {@link Dependent}, and all dependencies should be added to the class implementing this interface
 * with {@link #addDependent(Dependent)} or {@link #addDependents(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the
 * operation.
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
     * {@link Logger} for this dependent.
     */
    protected final Logger logger;

    /**
     * Set of {@link Dependent Dependencies} for this dependent.
     */
    protected final Set<Dependent> dependents;

    /**
     * @param logger {@link Logger} for this dependent
     */
    protected AbstractDependency(Logger logger) {
        this.logger = logger;
        dependents = new HashSet<>();
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public Collection<Dependent> getDependents() {
        return Collections.unmodifiableSet(dependents);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidateDependents() {
        dependents.forEach(Dependent::invalidate);
    }

    /**
     * {@inheritDoc}
     *
     * @param dependent {@inheritDoc}
     * @return {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
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
     * {@inheritDoc}
     *
     * @param dependents {@inheritDoc}
     * @return {@inheritDoc}
     *
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public boolean addDependents(Collection<? extends Dependent> dependents) {
        for(Dependent dependency : dependents) {
            if(DependencyUtils.checkCyclicDependencies(this, dependency)) {
                throw LogUtils.logExceptionAndGet(logger,
                        DependencyStrings.cyclicDetected(dependency, this),
                        IllegalArgumentException::new);
            }
        }

        return this.dependents.addAll(dependents);
    }

    /**
     * @param dependent {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean removeDependent(Dependent dependent) {
        return dependents.remove(dependent);
    }

    /**
     * @param dependents {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean removeDependents(Collection<? extends Dependent> dependents) {
        return this.dependents.removeAll(dependents);
    }

    /**
     * @param dependent {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean containsDependent(Dependent dependent) {
        return dependents.contains(dependent);
    }

    /**
     * @param dependents {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean containsDependents(Collection<? extends Dependent> dependents) {
        return this.dependents.containsAll(dependents);
    }

    /**
     * {@inheritDoc}
     *
     * @param dependent {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean containsDependentRecursive(Dependent dependent) {
        return DependencyUtils.containsDependencyRecursive(dependent, this);
    }

    /**
     * {@inheritDoc}
     *
     * @param dependents {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean containsDependentsRecursive(Collection<? extends Dependent> dependents) {
        return dependents.stream().allMatch(dependency ->
                DependencyUtils.containsDependencyRecursive(dependency, this));
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public Stream<Dependent> dependentStream() {
        return dependents.stream();
    }

    /**
     * @return {@inheritDoc}
     */
    @Override
    public Stream<Dependent> recursiveDependentStream() {
        return dependents.stream().flatMap(DependencyUtils::flatMapDependencies);
    }
}
