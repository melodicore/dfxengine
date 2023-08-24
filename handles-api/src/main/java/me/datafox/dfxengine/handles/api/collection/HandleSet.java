package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Collection;
import java.util.SortedSet;

/**
 * An extension of {@link SortedSet} that can only contain {@link Handle}s of a given {@link Space}.
 *
 * @author datafox
 */
public interface HandleSet extends SortedSet<Handle> {
    /**
     * @return {@link Space} associated with this set
     */
    Space getSpace();

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return true if this set contains a Handle with given id
     */
    boolean containsById(String id);

    /**
     * @param ids ids of the {@link Handle}s to be checked for
     * @return true if this set contains all Handles with given ids
     */
    boolean containsAllById(Collection<String> ids);

    /**
     * @param id id of the requested {@link Handle}
     * @return Handle matching the given id, or null if none are present
     */
    Handle get(String id);

    /**
     * @param id id of the {@link Handle} to be removed
     * @return true if the Handles of this set changed as a result of this action
     */
    boolean removeById(String id);

    /**
     * @param ids ids of the {@link Handle}s to be removed
     * @return true if the Handles of this set changed as a result of this action
     */
    boolean removeAllById(Collection<String> ids);
}
