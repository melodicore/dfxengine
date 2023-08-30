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
 * Abstract implementation of {@link Dependent}. Please note that the cyclic dependency detection in this class will
 * only detect dependent dependencies that implement both {@link Dependent} and {@link Dependency}. It is recommended
 * to extend {@link DependencyDependent} where possible for all cases where both interfaces would be implemented.
 *
 * @author datafox
 */
public abstract class AbstractDependent implements Dependent {
    protected final Logger logger;

    protected final Set<Dependency> dependencies;

    protected AbstractDependent(Logger logger) {
        this.logger = logger;
        dependencies = new HashSet<>();
    }

    protected AbstractDependent(Logger logger, Supplier<Set<Dependency>> set) {
        this.logger = logger;
        dependencies = set.get();
    }

    @Override
    public Collection<Dependency> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    @Override
    public void invalidateDependencies() {
        dependencies.forEach(Dependency::invalidate);
    }

    @Override
    public boolean addDependency(Dependency dependency) {
        if(DependencyUtils.checkCyclicDependencies(this, dependency)) {
            throw LogUtils.logExceptionAndGet(logger,
                DependencyStrings.cyclicDetected(dependency, this),
                IllegalArgumentException::new);
        }

        return dependencies.add(dependency);
    }

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

    @Override
    public boolean removeDependency(Dependency dependency) {
        return dependencies.remove(dependency);
    }

    @Override
    public boolean removeDependencies(Collection<Dependency> dependencies) {
        return this.dependencies.removeAll(dependencies);
    }

    @Override
    public boolean containsDependency(Dependency dependency) {
        return dependencies.contains(dependency);
    }

    @Override
    public boolean containsDependencies(Collection<Dependency> dependencies) {
        return this.dependencies.containsAll(dependencies);
    }

    @Override
    public boolean containsDependencyRecursive(Dependency dependency) {
        return DependencyUtils.containsDependencyRecursive(dependency, this);
    }

    @Override
    public boolean containsDependenciesRecursive(Collection<Dependency> dependencies) {
        return dependencies.stream().allMatch(dependency -> DependencyUtils.containsDependencyRecursive(dependency, this));
    }

    @Override
    public Stream<Dependency> dependencyStream() {
        return dependencies.stream();
    }

    @Override
    public Stream<Dependency> recursiveDependencyStream() {
        return dependencies.stream().flatMap(DependencyUtils::flatMapDependencies);
    }
}
