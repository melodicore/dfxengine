package me.datafox.dfxengine.handles.api;

/**
 * An interface denoting that an implementing class is associated with a {@link Handle}.
 *
 * @author datafox
 */
public interface Handled {
    /**
     * Returns the {@link Handle} associated with this class.
     *
     * @return Handle associated with this class
     */
    Handle getHandle();
}
