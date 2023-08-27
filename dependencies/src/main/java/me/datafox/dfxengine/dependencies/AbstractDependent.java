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
    public boolean addDependency(Dependency dependency) {
        if(DependencyUtils.checkCyclicDependencies(this, dependency)) {
            return dependencies.add(dependency);
        }

        throw LogUtils.logExceptionAndGet(logger,
                DependencyStrings.cyclicDetected(dependency, this),
                IllegalArgumentException::new);
    }

    @Override
    public boolean removeDependency(Dependency dependency) {
        return dependencies.remove(dependency);
    }

    @Override
    public boolean containsDependency(Dependency dependency) {
        return dependencies.contains(dependency);
    }

    @Override
    public Stream<Dependency> dependencyStream() {
        return dependencies.stream();
    }
}
