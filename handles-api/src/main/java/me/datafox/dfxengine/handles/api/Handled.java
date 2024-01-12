package me.datafox.dfxengine.handles.api;

/**
 * An interface denoting that an implementing class is associated with a {@link Handle}.
 *
 * @author datafox
 */
public interface Handled {
    /**
     * @return {@link Handle} associated with this class
     */
    Handle getHandle();
}
