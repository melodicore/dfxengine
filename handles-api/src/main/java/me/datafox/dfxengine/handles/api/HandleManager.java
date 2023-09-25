package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * A singleton class for managing {@link Handle Handles} and {@link Space Spaces}. Contains an ordered collection of
 * Spaces, order of which is determined by those Spaces' identifying Handles. Must define two special Spaces, one for
 * the Handles identifying Spaces, and one for the Handles used as tags.
 *
 * @author datafox
 */
public interface HandleManager {
    /**
     * @param id id of the requested {@link Handle}
     * @return {@link Space} identification {@link Handle} matching the specified id, or {@code null} if none are
     * present
     */
    Handle getSpaceHandle(String id);

    /**
     * @return all identifying {@link Handle Handles} of the {@link Space Spaces} present in this handle manager
     */
    Collection<Handle> getSpaceHandles();

    /**
     * @param handle {@link Handle} of the requested {@link Space}
     * @return contained {@link Space} matching the specified {@link Handle}, or {@code null} if none are present
     */
    Space getSpace(Handle handle);

    /**
     * @param id id of the requested {@link Space}
     * @return contained {@link Space} matching the specified id, or {@code null} if none are present
     */
    Space getSpaceById(String id);

    /**
     * @return all {@link Space Spaces} present in this handle manager
     */
    Collection<Space> getSpaces();

    /**
     * @param id id of the {@link Space} to be created
     * @return created {@link Space}
     *
     * @throws IllegalArgumentException if a {@link Space} with the specified id is already present
     */
    Space createSpace(String id);

    /**
     * Checks if a {@link Space} is present with the specified id. If one is present, that Space is returned. If none
     * are present, a new Space with the specified id is created and returned.
     *
     * @param id id of the requested {@link Space}
     * @return requested {@link Space}
     */
    Space getOrCreateSpace(String id);

    /**
     * @param space {@link Space} to be checked for
     * @return {@code true} if this handle manager contains the specified {@link Space}
     */
    boolean containsSpace(Space space);

    /**
     * @param handle identifying {@link Handle} of the {@link Space} to be checked for
     * @return {@code true} if this handle manager contains a {@link Space} with the specified {@link Handle}
     */
    boolean containsSpaceByHandle(Handle handle);

    /**
     * @param id id of the {@link Space} to be checked for
     * @return {@code true} if this handle manager contains a {@link Space} with the specified id
     */
    boolean containsSpaceById(String id);

    /**
     * @param spaces {@link Space Spaces} to be checked for
     * @return {@code true} if this handle manager contains all the specified {@link Space Spaces}
     */
    boolean containsSpaces(Collection<Space> spaces);

    /**
     * @param handles identifying {@link Handle Handles} of the {@link Space Spaces} to be checked for
     * @return {@code true} if this handle manager contains {@link Space Spaces} with all the specified
     * {@link Handle Handles}
     */
    boolean containsSpacesByHandle(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Space Spaces} to be checked for
     * @return {@code true} if this handle manager contains {@link Space Spaces} with all the specified ids
     */
    boolean containsSpacesById(Collection<String> ids);

    /**
     * @param space {@link Space} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    boolean removeSpace(Space space);

    /**
     * @param handle identifying {@link Handle} of the {@link Space} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    boolean removeSpaceByHandle(Handle handle);

    /**
     * @param id id of the {@link Space} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    boolean removeSpaceById(String id);

    /**
     * @param spaces {@link Space Spaces} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    boolean removeSpaces(Collection<Space> spaces);

    /**
     * @param handles {@link Handle Handles} of the {@link Space Spaces} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    boolean removeSpacesByHandle(Collection<Handle> handles);

    /**
     * @param ids ids of the {@link Space Spaces} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    boolean removeSpacesById(Collection<String> ids);

    /**
     * @return {@link Stream} of the {@link Space Spaces} present in this handle manager
     */
    Stream<Space> spaceStream();

    /**
     * @param id id of the requested tag {@link Handle}
     * @return tag {@link Handle} matching the specified id, or {@code null} if none are present
     */
    Handle getTag(String id);

    /**
     * @return all tag {@link Handle Handles} present in this handle manager
     */
    Collection<Handle> getTags();

    /**
     * @param id id of the tag {@link Handle} to be created
     * @return created {@link Handle}
     *
     * @throws IllegalArgumentException if a tag Handle with the specified id is already present
     */
    Handle createTag(String id);

    /**
     * Checks if a tag {@link Handle} is present with the specified id. If one is present, that tag Handle is returned.
     * If none are present, a new tag Handle with the specified id is created and returned.
     *
     * @param id id of the requested tag {@link Handle}
     * @return requested tag {@link Handle}
     */
    Handle getOrCreateTag(String id);

    /**
     * @param tag tag {@link Handle} to be checked for
     * @return {@code true} if this handle manager contains the specified tag {@link Handle}
     */
    boolean containsTag(Handle tag);

    /**
     * @param id id of the tag {@link Handle} to be checked for
     * @return {@code true} if this handle manager contains a tag {@link Handle} with the specified id
     */
    boolean containsTagById(String id);

    /**
     * @param tags tag {@link Handle Handles} to be checked for
     * @return {@code true} if this handle manager contains all the specified tag {@link Handle Handles}
     */
    boolean containsTags(Collection<Handle> tags);

    /**
     * @param ids tag {@link Handle} ids to be checked for
     * @return {@code true} if this handle manager contains tag {@link Handle Handles} with all the specified ids
     */
    boolean containsTagsById(Collection<String> ids);

    /**
     * @param tag tag {@link Handle} to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    boolean removeTag(Handle tag);

    /**
     * @param id id of the tag {@link Handle} to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    boolean removeTagById(String id);

    /**
     * @param tags tag {@link Handle Handles} to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    boolean removeTags(Collection<Handle> tags);

    /**
     * @param ids tag {@link Handle} ids to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    boolean removeTagsById(Collection<String> ids);

    /**
     * @return {@link Stream} of the tag {@link Handle Handles} present in this handle manager
     */
    Stream<Handle> tagStream();

    /**
     * Clears everything and retains or reinstates the two hardcoded {@link Space Spaces} and their identifying
     * {@link Handle Handles}.
     */
    void clear();
}
