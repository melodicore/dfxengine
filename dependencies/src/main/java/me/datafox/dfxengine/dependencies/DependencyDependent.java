package me.datafox.dfxengine.dependencies;

import org.slf4j.Logger;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @author datafox
 */
public abstract class DependencyDependent extends AbstractDependent implements Dependency {
    protected DependencyDependent(Logger logger) {
        super(logger);
    }

    protected DependencyDependent(Logger logger, Supplier<Set<Dependency>> set) {
        super(logger, set);
    }

    @Override
    public void invalidate() {
        invalidateDependencies();
    }
}
