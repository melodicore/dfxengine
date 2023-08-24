package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A namespace for {@link Handle}s. Contains an identifying Handle and an ordered collection of Handles. The identifying
 * Handle is contained within a special hardcoded Space, details of which are documented in {@link HandleManager}.
 * Additionally, contains a reference to the managing HandleManager.
 *
 * @author datafox
 */
public interface Space extends Comparable<Space> {
    /**
     * @return {@link HandleManager} associated with this Space
     */
    HandleManager getHandleManager();

    /**
     * {@link Handle} identifying this Space, present in a hardcoded Space, details of which are documented in
     * {@link HandleManager}.
     *
     * @return Handle identifying this Space
     */
    Handle getHandle();

    /**
     * @return id of this Space, and by definition its identifying {@link Handle}
     */
    String getId();

    /**
     * @param handle {@link Handle} to be checked
     * @return true if the given Handle matches the identifying Handle of this Space
     */
    boolean isHandle(Handle handle);

    /**
     * @param id id to be checked
     * @return true if the given id matches the id of this Space
     */
    boolean isId(String id);

    /**
     * @param id id of the requested {@link Handle}
     * @return contained Handle matching the given id, or null if none are present
     */
    Handle getHandle(String id);

    /**
     * @return {@link Handle}s present in this Space
     */
    Collection<Handle> getHandles();

    /**
     * @param tag tag to be checked for
     * @return {@link Handle}s present in this Space that have given tag
     */
    Collection<Handle> getHandlesByTag(Handle tag);


    /**
     * @param id id of a tag to be checked for
     * @return {@link Handle}s present in this Space that have given tag
     */
    Collection<Handle> getHandlesByTagId(String id);

    /**
     * @param tags tags to be checked for
     * @return {@link Handle}s present in this Space that have all given tags
     */
    Collection<Handle> getHandlesByTags(Collection<Handle> tags);

    /**
     * @param ids tags to be checked for
     * @return {@link Handle}s present in this Space that have tags with all given ids
     */
    Collection<Handle> getHandlesByTagIds(Collection<String> ids);

    /**
     * @param id id of the {@link Handle} to be created
     * @return created Handle
     *
     * @throws IllegalArgumentException if a Handle with the given id is already present
     */
    Handle createHandle(String id);

    /**
     * Checks if a {@link Handle} is present with the given id. If one is present, that Handle is returned. If none are
     * present, a new Handle with the given id is created and returned.
     *
     * @param id id of the requested Handle
     * @return requested Handle
     */
    Handle getOrCreateHandle(String id);

    /**
     * @param handle {@link Handle} to be checked for
     * @return true if this Space contains given Handle
     */
    boolean containsHandle(Handle handle);

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return true if this Space contains a Handle with given id
     */
    boolean containsHandleById(String id);

    /**
     * @param handles {@link Handle}s to be checked for
     * @return true if this Space contains all given Handles
     */
    boolean containsHandles(Collection<Handle> handles);

    /**
     * @param ids {@link Handle} ids to be checked for
     * @return true if this Space contains Handles with all given ids
     */
    boolean containsHandlesById(Collection<String> ids);

    /**
     * @param handle {@link Handle} to be removed
     * @return true if the Handles of this Space changed as a result of this action
     */
    boolean removeHandle(Handle handle);

    /**
     * @param id ids of the {@link Handle} to be removed
     * @return true if the Handles of this Space changed as a result of this action
     */
    boolean removeHandleById(String id);

    /**
     * @param handles {@link Handle}s to be removed
     * @return true if the Handles of this Space changed as a result of this action
     */
    boolean removeHandles(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Handle} to be removed
     * @return true if the Handles of this Space changed as a result of this action
     */
    boolean removeHandlesById(Collection<String> ids);

    /**
     * @return {@link Stream} of the {@link Handle}s present in this Space
     */
    Stream<Handle> handleStream();

    /**
     * Clears all {@link Handle}s from this Space.
     */
    void clear();

    @Override
    default int compareTo(Space o) {
        return getHandle().compareTo(o.getHandle());
    }
}
