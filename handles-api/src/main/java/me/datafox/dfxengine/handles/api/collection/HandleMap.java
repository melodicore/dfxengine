package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * An extension of {@link Map} that can only use {@link Handle Handles} of the {@link Space} associated with this map as
 * its keys.
 *
 * @author datafox
 */
public interface HandleMap<T> extends Map<Handle,T> {
    /**
     * @return {@link Space} associated with this map
     */
    Space getSpace();

    /**
     * Associates the specified value with its associated {@link Handle} in this map. If the map previously contained a
     * mapping for the key, the old value is replaced. Because Java does not support union types, the specified value
     * must implement {@link Handled}, and an exception is thrown otherwise.
     *
     * @param value value implementing {@link Handled} to be associated in this map with its associated {@link Handle}
     * as a key
     * @return the previously associated value in this map, or {@code null} if there was no previous association
     *
     * @throws IllegalArgumentException if the specified value does not implement {@link Handled}, or if the associated
     * {@link Handle} is not contained within the {@link Space} associated with this map
     */
     T putHandled(T value);

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return {@code true} if this map contains a {@link Handle} with the specified id
     */
    boolean containsById(String id);

    /**
     * @param handles {@link Handle Handles} to be checked for
     * @return {@code true} if this map contains all the specified {@link Handle Handles}
     */
    boolean containsAll(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Handle Handles} to be checked for
     * @return {@code true} if this map contains all {@link Handle Handles} with the specified ids
     */
    boolean containsAllById(Collection<String> ids);

    /**
     * @param id id of the requested {@link Handle}
     * @return value matching the {@link Handle} with the specified id, or {@code null} if none are present
     */
    T getById(String id);

    /**
     * @param id id of the {@link Handle} to be removed
     * @return the value associated with the {@link Handle} with the specified id, or {@code null} if none were present
     */
    T removeById(String id);

    /**
     * @param handles {@link Handle Handles} to be removed
     * @return {@code true} if the contents of this map changed as a result of this action
     */
    boolean removeAll(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Handle Handles} to be removed
     * @return {@code true} if the contents of this map changed as a result of this action
     */
    boolean removeAllById(Collection<String> ids);

    /**
     * @return {@link Stream} of the values present in this map
     */
    default Stream<T> stream() {
        return values().stream();
    }
}
