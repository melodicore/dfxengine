package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A singleton class for managing {@link Handle}s and {@link Space}s. Contains an ordered collection of Spaces, order of
 * which is determined by those Spaces' identifying Handles. Must define two special Spaces, one for the Handles
 * identifying Spaces, and one for the Handles used as tags.
 *
 * @author datafox
 */
public interface HandleManager {
    /**
     * @param id id of the requested {@link Handle)
     * @return {@link Space} identification Handle matching the given id, or null if none are present
     */
    Handle getSpaceHandle(String id);

    /**
     * @return identifying {@link Handle)s of the {@link Space}s present in this HandleManager
     */
    Collection<Handle> getSpaceHandles();

    /**
     * @param handle {@link Handle} of the requested Space
     * @return contained Space matching the given Handle, or null if none are present
     */
    Space getSpace(Handle handle);

    /**
     * @param id id of the requested {@link Space}
     * @return contained Space matching the given id, or null if none are present
     */
    Space getSpaceById(String id);

    /**
     * @return {@link Space}s present in this HandleManager
     */
    Collection<Space> getSpaces();

    /**
     * @param id id of the {@link Space} to be created
     * @return created Space
     *
     * @throws IllegalArgumentException if a Space with given id is already present
     */
    Space createSpace(String id);

    /**
     * Checks if a {@link Space} is present with the given id. If one is present, that Space is returned. If none are
     * present, a new Space with the given id is created and returned.
     *
     * @param id id of the requested Space
     * @return requested Space
     */
    Space getOrCreateSpace(String id);

    /**
     * @param space {@link Space} to be checked for
     * @return true if this HandleManager contains given Space
     */
    boolean containsSpace(Space space);

    /**
     * @param handle identifying {@link Handle} of the {@link Space} to be checked for
     * @return true if this HandleManager contains a Space with given Handle
     */
    boolean containsSpaceByHandle(Handle handle);

    /**
     * @param id id of the {@link Space} to be checked for
     * @return true if this HandleManager contains a Space with given id
     */
    boolean containsSpaceById(String id);

    /**
     * @param spaces {@link Space}s to be checked for
     * @return true if this HandleManager contains all given Spaces
     */
    boolean containsSpaces(Collection<Space> spaces);

    /**
     * @param handles identifying {@link Handle}s of the {@link Space}s to be checked for
     * @return true if this HandleManager contains Spaces with all given Handles
     */
    boolean containsSpacesByHandle(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Space}s to be checked for
     * @return true if this HandleManager contains Spaces with all given ids
     */
    boolean containsSpacesById(Collection<String> ids);

    /**
     * @param space {@link Space} to be removed
     * @return true if the Spaces of this HandleManager changed as a result of this action
     */
    boolean removeSpace(Space space);

    /**
     * @param handle identifying {@link Handle} of the {@link Space} to be removed
     * @return true if the Spaces of this HandleManager changed as a result of this action
     */
    boolean removeSpaceByHandle(Handle handle);

    /**
     * @param id id of the {@link Space} to be removed
     * @return true if the Spaces of this HandleManager changed as a result of this action
     */
    boolean removeSpaceById(String id);

    /**
     * @param spaces {@link Space}s to be removed
     * @return true if the Spaces of this HandleManager changed as a result of this action
     */
    boolean removeSpaces(Collection<Space> spaces);

    /**
     * @param handles {@link Handle}s of the {@link Space}s to be removed
     * @return true if the Spaces of this HandleManager changed as a result of this action
     */
    boolean removeSpacesByHandle(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Space}s to be removed
     * @return true if the Spaces of this HandleManager changed as a result of this action
     */
    boolean removeSpacesById(Collection<String> ids);

    /**
     * @return {@link Stream} of the {@link Space}s present in this HandleManager
     */
    Stream<Space> spaceStream();

    /**
     * @param id id of the requested tag {@link Handle}
     * @return tag Handle matching the given id, or null if none are present
     */
    Handle getTag(String id);

    /**
     * @return tag {@link Handle}s present in this HandleManager
     */
    Collection<Handle> getTags();

    /**
     * @param id id of the tag {@link Handle} to be created
     * @return created Handle
     *
     * @throws IllegalArgumentException if a tag Handle with given id is already present
     */
    Handle createTag(String id);

    /**
     * Checks if a tag {@link Handle} is present with the given id. If one is present, that tag Handle is returned. If
     * none are present, a new tag Handle with the given id is created and returned.
     *
     * @param id id of the requested tag Handle
     * @return requested tag Handle
     */
    Handle getOrCreateTag(String id);

    /**
     * @param tag tag {@link Handle} to be checked for
     * @return true if this HandleManager contains given tag Handle
     */
    boolean containsTag(Handle tag);

    /**
     * @param id id of the tag {@link Handle} to be checked for
     * @return true if this HandleManager contains a tag Handle with given id
     */
    boolean containsTagById(String id);

    /**
     * @param tags tag {@link Handle}s to be checked for
     * @return true if this HandleManager contains all given tag Handles
     */
    boolean containsTags(Collection<Handle> tags);

    /**
     * @param ids tag {@link Handle} ids to be checked for
     * @return true if this HandleManager contains tag Handles with all given ids
     */
    boolean containsTagsById(Collection<String> ids);

    /**
     * @param tag tag {@link Handle} to be removed
     * @return true if the tag Handles of this HandleManager changed as a result of this action
     */
    boolean removeTag(Handle tag);

    /**
     * @param id id of the tag {@link Handle} to be removed
     * @return true if the tag Handles of this HandleManager changed as a result of this action
     */
    boolean removeTagById(String id);

    /**
     * @param tags tag {@link Handle}s to be removed
     * @return true if the tag Handles of this HandleManager changed as a result of this action
     */
    boolean removeTags(Collection<Handle> tags);

    /**
     * @param ids tag {@link Handle} ids to be removed
     * @return true if the tag Handles of this HandleManager changed as a result of this action
     */
    boolean removeTagsById(Collection<String> ids);

    /**
     * @return {@link Stream} of the tag {@link Handle}s present in this HandleManager
     */
    Stream<Handle> tagStream();

    /**
     * Clears everything and retains or reinstates the two hardcoded {@link Space}s and their identifying
     * {@link Handle}s.
     */
    void clear();
}
