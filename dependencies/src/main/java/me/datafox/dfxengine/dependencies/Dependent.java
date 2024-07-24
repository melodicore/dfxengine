package me.datafox.dfxengine.dependencies;

/**
 * <p>
 * A simple dependent that can be invalidated. Invalidation should generally be only called by
 * {@link Dependency#invalidateDependents()}. If a class that implements this interface also implements
 * {@link Dependency}, {@link #invalidate()} must call {@link Dependency#invalidateDependents()} on itself.
 * </p>
 * <p>
 * In general a dependent is implemented on any class that has a return value which is dependent on values of other
 * classes. These other classes should implement {@link Dependency}, and the dependent should be added to them with
 * {@link Dependency#addDependent(Dependent)}. The dependent can then cache the return value, and invalidate that
 * cache whenever {@link #invalidate()} is called, and the {@link Dependency Dependencies} should call it whenever
 * something in them changes, usually by calling {@link Dependency#invalidateDependents()}.
 * </p>
 *
 * @author datafox
 */
public interface Dependent {
    /**
     * Invalidates the class implementing this interface. In practice, this should invalidate the caches of any values
     * that are dependent on values of other classes. Because this method may be called multiple times, it is not
     * recommended to recalculate the cached value in this method, and instead create an invalidated flag that is set
     * in this method, and recalculate the value in its getter method if the flag is set.
     */
    void invalidate();
}
