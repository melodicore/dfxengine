package me.datafox.dfxengine.handles.api;

/**
 * An interface determining that any class implementing it should be identified with a {@link Handle}.
 *
 * @author datafox
 */
public interface Handled {
    /**
     * @return identifying {@link Handle} of this object
     */
    Handle getHandle();
}
