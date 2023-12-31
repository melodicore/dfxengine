package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A namespace for {@link Handle Handles}. Contains an identifying Handle and an ordered collection of Handles. The
 * identifying Handle is contained in a special hardcoded space, details of which are documented in
 * {@link HandleManager}. Additionally, contains a reference to the associated HandleManager.
 *
 * @author datafox
 */
public interface Space extends Comparable<Space> {
    /**
     * @return {@link HandleManager} associated with this space
     */
    HandleManager getHandleManager();

    /**
     * {@link Handle} identifying this space, present in a hardcoded space, details of which are documented in
     * {@link HandleManager}.
     *
     * @return {@link Handle} identifying this space
     */
    Handle getHandle();

    /**
     * @return id of this space, and by definition its identifying {@link Handle}
     */
    String getId();

    /**
     * @param handle {@link Handle} to be checked
     * @return {@code true} if the specified {@link Handle} matches the identifying Handle of this space
     */
    boolean isHandle(Handle handle);

    /**
     * @param id id to be checked
     * @return {@code true} if the specified id matches the id of this space
     */
    boolean isId(String id);

    /**
     * @param id id of the requested {@link Handle}
     * @return contained {@link Handle} matching the specified id, or {@code null} if none are present
     */
    Handle getHandle(String id);

    /**
     * @return all {@link Handle Handles} present in this space
     */
    Collection<Handle> getHandles();

    /**
     * @param tag tag to be checked for
     * @return all {@link Handle Handles} present in this space that contain the specified tag
     */
    Collection<Handle> getHandlesByTag(Handle tag);

    /**
     * @param id id of a tag to be checked for
     * @return all {@link Handle Handles} present in this space that contain the specified tag
     */
    Collection<Handle> getHandlesByTagId(String id);

    /**
     * @param tags tags to be checked for
     * @return all {@link Handle Handles} present in this space that contain all the specified tags
     */
    Collection<Handle> getHandlesByTags(Collection<Handle> tags);

    /**
     * @param ids tags to be checked for
     * @return all {@link Handle Handles} present in this space that contain tags with all the specified ids
     */
    Collection<Handle> getHandlesByTagIds(Collection<String> ids);

    /**
     * @param id id of the {@link Handle} to be created
     * @return created {@link Handle}
     *
     * @throws IllegalArgumentException if a {@link Handle} with the specified id is already present
     */
    Handle createHandle(String id);

    /**
     * Checks if a {@link Handle} is present with the specified id. If one is present, that Handle is returned. If none
     * are present, a new Handle with the specified id is created and returned.
     *
     * @param id id of the requested {@link Handle}
     * @return requested {@link Handle}
     */
    Handle getOrCreateHandle(String id);

    /**
     * @param handle {@link Handle} to be checked for
     * @return {@code true} if this space contains the specified {@link Handle}
     */
    boolean containsHandle(Handle handle);

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return {@code true} if this space contains a {@link Handle} with the specified id
     */
    boolean containsHandleById(String id);

    /**
     * @param handles {@link Handle Handles} to be checked for
     * @return {@code true} if this space contains all the specified {@link Handle Handles}
     */
    boolean containsHandles(Collection<Handle> handles);

    /**
     * @param ids {@link Handle} ids to be checked for
     * @return {@code true} if this space contains {@link Handle Handles} with all the specified ids
     */
    boolean containsHandlesById(Collection<String> ids);

    /**
     * @param handle {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    boolean removeHandle(Handle handle);

    /**
     * @param id ids of the {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    boolean removeHandleById(String id);

    /**
     * @param handles {@link Handle Handles} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    boolean removeHandles(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    boolean removeHandlesById(Collection<String> ids);

    /**
     * @return {@link Stream} of the {@link Handle Handles} present in this space
     */
    Stream<Handle> handleStream();

    /**
     * Clears all {@link Handle Handles} from this space.
     */
    void clear();

    /**
     * Compares this space with the specified space for order. The spaces are compared with their identifying
     * {@link Handle Handles} using {@link Handle#compareTo(Handle)}. Returns a negative  integer, zero, or a positive
     * integer as this space is less than, equal to, or greater than the specified space.
     *
     * @param other the space to be compared
     * @return a negative integer, zero, or a positive integer as this space is less than, equal to, or greater than
     * the specified space
     */
    @Override
    default int compareTo(Space other) {
        return getHandle().compareTo(other.getHandle());
    }
}
