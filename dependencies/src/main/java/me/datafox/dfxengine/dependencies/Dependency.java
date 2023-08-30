package me.datafox.dfxengine.dependencies;

/**
 * <p>
 * A simple dependency that can be invalidated. Invalidation should generally be only called by
 * {@link Dependent#invalidateDependencies()}. If a class that implements this interface also implements
 * {@link Dependent}, {@link #invalidate()} must call {@link Dependent#invalidateDependencies()} on itself.
 * </p>
 * <p>
 * In general a Dependency is implemented on any class that has a return value which is dependent on values of other
 * classes. These other classes should implement {@link Dependent}, and the dependency should be added to them with
 * {@link Dependent#addDependency(Dependency)}. The dependency can then cache the return value, and invalidate that
 * cache whenever {@link #invalidate()} is called, and the {@link Dependent}s should call it whenever something in them
 * changes, usually by calling {@link Dependent#invalidateDependencies()}.
 * </p>
 * @author datafox
 */
public interface Dependency {
    /**
     * Invalidates the class implementing this interface. In practice, this should invalidate the caches of any values
     * that are dependent on values of other classes. Because this method may be called multiple times, it is
     * recommended to not recalculate the cached value in this method, and instead create an invalidated flag that is
     * set in this method, and recalculate the value in its get method if the flag is set.
     */
    void invalidate();
}
