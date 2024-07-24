package me.datafox.dfxengine.dependencies;

import org.slf4j.Logger;

import java.util.Collection;

/**
 * <p>
 * A class with values that values of other classes depend on. A class with values depending on the implementing class
 * should implement {@link Dependent}, and all dependencies should be added to the class implementing this interface
 * with {@link #addDependent(Dependent)} or {@link #addDependents(Collection)}. The implementation of these methods
 * must check for cyclic dependencies and throw {@link IllegalArgumentException} if one would be caused by the
 * operation.
 * </p>
 * <p>
 * This class also implements {@link Dependent}. Calls {@link Dependency#invalidateDependents()} when the implemented
 * {@link Dependent#invalidate()} is called. This class is the recommended way to create dependency graphs spanning
 * multiple levels.
 * </p>
 * <p>
 * The cyclic dependency detection in this class will only detect dependent dependencies that implement both
 * {@link Dependency} and {@link Dependent}.
 * </p>
 *
 * @author datafox
 */
public abstract class DependencyDependent extends AbstractDependency implements Dependent {
    /**
     * @param logger {@link Logger} for this dependent
     */
    protected DependencyDependent(Logger logger) {
        super(logger);
    }

    /**
     * {@inheritDoc} Instead of overriding this method, {@link #onInvalidate()} should be overridden instead.
     */
    @Override
    public void invalidate() {
        onInvalidate();
        invalidateDependents();
    }

    /**
     * This method should be treated as the equivalent of {@link Dependent#invalidate()} for classes extending
     * DependencyDependent. This exists to avoid the need for calling {@code super.invalidate()} or
     * {@link #invalidateDependents()} which may cause hidden or hard to find bugs when forgotten.
     */
    protected abstract void onInvalidate();
}
