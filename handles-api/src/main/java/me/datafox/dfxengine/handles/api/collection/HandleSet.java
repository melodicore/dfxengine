package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Collection;
import java.util.Set;

/**
 * An extension of {@link Set} that can only contain {@link Handle Handles} of the {@link Space} associated with this
 * set.
 *
 * @author datafox
 */
public interface HandleSet extends Set<Handle> {
    /**
     * @return {@link Space} associated with this set
     */
    Space getSpace();

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return {@code true} if this set contains a {@link Handle} with the specified id
     */
    boolean containsById(String id);

    /**
     * @param ids ids of the {@link Handle Handles} to be checked for
     * @return {@code true} if this set contains all {@link Handle Handles} with the specified ids
     */
    boolean containsAllById(Collection<String> ids);

    /**
     * @param id id of the requested {@link Handle}
     * @return {@link Handle} matching the specified id, or {@code null} if none are present
     */
    Handle get(String id);

    /**
     * @param id id of the {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this set changed as a result of this action
     */
    boolean removeById(String id);

    /**
     * @param ids ids of the {@link Handle Handles} to be removed
     * @return {@code true} if the {@link Handle Handles} of this set changed as a result of this action
     */
    boolean removeAllById(Collection<String> ids);
}
