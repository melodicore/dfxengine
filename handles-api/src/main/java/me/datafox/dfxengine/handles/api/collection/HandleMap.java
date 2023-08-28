package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An extension of {@link Map} that can only use {@link Handle}s of the {@link Space} associated with this map as its
 * keys.
 *
 * @author datafox
 */
public interface HandleMap<T> extends Map<Handle,T> {
    /**
     * @return {@link Space} associated with this map
     */
    Space getSpace();

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return true if this map contains a Handle with the specified id
     */
    boolean containsById(String id);

    /**
     * @param handles {@link Handle}s to be checked for
     * @return true if this map contains all the specified Handles
     */
    boolean containsAll(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Handle}s to be checked for
     * @return true if this map contains all Handles with the specified ids
     */
    boolean containsAllById(Collection<String> ids);

    /**
     * @param id id of the requested {@link Handle}
     * @return value matching the Handle with the specified id, or null if none are present
     */
    T getById(String id);

    /**
     * @param id id of the {@link Handle} to be removed
     * @return the value associated with the Handle with the specified id, or null if none were present
     */
    T removeById(String id);

    /**
     * @param handles {@link Handle}s to be removed
     * @return true if the contents of this map changed as a result of this action
     */
    boolean removeAll(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Handle}s to be removed
     * @return true if the contents of this map changed as a result of this action
     */
    boolean removeAllById(Collection<String> ids);

    /**
     * @return {@link Stream} of the values present in this map
     */
    default Stream<T> stream() {
        return values().stream();
    }
}
