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
    /**
     * @param logger {@link Logger} for this {@link Dependent}
     */
    protected DependencyDependent(Logger logger) {
        super(logger);
    }

    /**
     * @param logger {@link Logger} for this {@link Dependent}
     * @param set {@link Supplier} for a {@link Set} containing {@link Dependency Dependencies} to be used as the
     * backing set
     */
    protected DependencyDependent(Logger logger, Supplier<Set<Dependency>> set) {
        super(logger, set);
    }

    /**
     * <p>
     * Invalidates the class implementing this interface. In practice, this should invalidate the caches of any values
     * that are dependent on values of other classes. Because this method may be called multiple times, it is not
     * recommended to recalculate the cached value in this method, and instead create an invalidated flag that is set
     * in this method, and recalculate the value in its getter method if the flag is set.
     * </p>
     * <p>
     * Instead of overriding this method, {@link #onInvalidate()} should be overridden instead.
     * </p>
     */
    @Override
    public void invalidate() {
        onInvalidate();
        invalidateDependencies();
    }

    /**
     * This method should be treated as the equivalent of {@link Dependency#invalidate()} for classes extending
     * DependencyDependent. This exists to avoid the need for calling {@code super.invalidate()} or
     * {@link #invalidateDependencies()} which may cause hidden or hard to find bugs when forgotten.
     */
    protected abstract void onInvalidate();
}
