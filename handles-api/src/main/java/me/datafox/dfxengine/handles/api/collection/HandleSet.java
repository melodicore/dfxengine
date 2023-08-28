package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Collection;
import java.util.Set;

/**
 * An extension of {@link Set} that can only contain {@link Handle}s of the {@link Space} associated with this set.
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
     * @return true if this set contains a Handle with the specified id
     */
    boolean containsById(String id);

    /**
     * @param ids ids of the {@link Handle}s to be checked for
     * @return true if this set contains all Handles with the specified ids
     */
    boolean containsAllById(Collection<String> ids);

    /**
     * @param id id of the requested {@link Handle}
     * @return Handle matching the specified id, or null if none are present
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
