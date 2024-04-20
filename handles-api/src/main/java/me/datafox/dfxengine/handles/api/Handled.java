package me.datafox.dfxengine.handles.api;

/**
 * An interface determining that any class implementing it should be identified with a {@link Handle}.
 *
 * @author datafox
 */
public interface Handled extends Comparable<Handled> {
    /**
     * @return identifying {@link Handle} of this object
     */
    Handle getHandle();

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than the specified object. The order of objects is specified
     * by their handles, using {@link Handle#compareTo(Handle)}.
     *
     * @param o object to be compared.
     * @return negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the
     * specified object
     * @throws NullPointerException if the specified object is {@code null}
     */
    @Override
    default int compareTo(Handled o) {
        return getHandle().compareTo(o.getHandle());
    }
}
