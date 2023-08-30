package me.datafox.dfxengine.dependencies;

import org.slf4j.Logger;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Abstract extension of {@link AbstractDependent} that also implements {@link Dependency}. Calls
 * {@link Dependent#invalidateDependencies()} when the implemented {@link Dependency#invalidate()} is called. This class
 * is the recommended way to create dependency graphs spanning multiple levels.
 *
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
        onInvalidate();
        invalidateDependencies();
    }

    /**
     * This method should be treated as the equivalent of {@link Dependency#invalidate()} for classes extending
     * DependencyDependent. This exists to avoid the need for calling <code>super.invalidate()</code> or
     * {@link Dependent#invalidateDependencies()} which may cause hidden or hard to find bugs when forgotten.
     */
    protected abstract void onInvalidate();
}
