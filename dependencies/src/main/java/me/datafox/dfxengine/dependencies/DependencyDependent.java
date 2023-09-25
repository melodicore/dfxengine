package me.datafox.dfxengine.dependencies;

import org.slf4j.Logger;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

/**
 * <p>
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependency}, and all Dependencies should be added to the class implementing this interface
 * with {@link #addDependency(Dependency)} or {@link #addDependencies(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the
 * operation.
 * </p>
 * <p>
 * This class also implements {@link Dependency}. Calls {@link Dependent#invalidateDependencies()} when the implemented
 * {@link Dependency#invalidate()} is called. This class is the recommended way to create dependency graphs spanning
 * multiple levels.
 * </p>
 * <p>
 * The cyclic dependency detection in this class will only detect dependent dependencies that implement both
 * {@link Dependent} and {@link Dependency}.
 * </p>
 *
 * @author datafox
 */
public abstract class DependencyDependent extends AbstractDependent implements Dependency {
    /**
     * @param logger {@link Logger} for this dependent
     */
    protected DependencyDependent(Logger logger) {
        super(logger);
    }

    /**
     * @param logger {@link Logger} for this dependent
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
