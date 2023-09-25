package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * An identifier class with a tagging system. A handle in its most bare-bones understanding contains a {@link String}
 * id, a {@link Space} which is a namespace for ids, and zero or more tags, which are also handles. In practice a handle
 * also contains an index used for ordering, and a reference to a {@link HandleManager} which is used to manipulate
 * handles and Spaces, including tags. Handles are primarily designed to be used as map keys and for other identifying
 * purposes.
 *
 * @author datafox
 */
public interface Handle extends Comparable<Handle> {
    /**
     * @return {@link HandleManager} associated with this handle
     */
    HandleManager getHandleManager();

    /**
     * @return {@link Space} containing this handle
     */
    Space getSpace();

    /**
     * @return {@link String} id of this handle
     */
    String getId();

    /**
     * The index value used for ordering handles in a {@link Space}.
     *
     * @return index of this handle
     */
    long getIndex();

    /**
     * @param id id to be checked for
     * @return {@code true} if the specified id matches the id of this handle
     */
    boolean isId(String id);

    /**
     * @return all tags associated with this handle
     */
    Collection<Handle> getTags();

    /**
     * @param tag handle to be added as a tag
     * @return {@code true} if the tags of this handle changed as a result of this action
     *
     * @throws IllegalArgumentException when the specified handle is not a part of the tags {@link Space}. Details for
     * hardcoded Spaces are documented in {@link HandleManager}
     */
    boolean addTag(Handle tag);

    /**
     * Adds a tag based on the specified id for the tag. If a tag with the specified id does not exist in the tags
     * {@link Space}, one is automatically created. Details for hardcoded Spaces are documented in
     * {@link HandleManager}.
     *
     * @param id id of the handle to be added as a tag
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    boolean addTagById(String id);

    /**
     * @param tags handles to be added as a tags
     * @return {@code true} if the tags of this handle changed as a result of this action
     *
     * @throws IllegalArgumentException when any of the specified handles are not a part of the tags {@link Space}.
     * Details for hardcoded Spaces are documented in {@link HandleManager}
     */
    boolean addTags(Collection<Handle> tags);

    /**
     * Adds tags based on the specified ids for the tags. If a tag with the specified id does not exist in the tags
     * {@link Space}, one is automatically created. Details for hardcoded Spaces are documented in
     * {@link HandleManager}.
     *
     * @param ids ids for the handles to be added as a tags
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    boolean addTagsById(Collection<String> ids);

    /**
     * @param tag tag to be checked for
     * @return {@code true} if this handle contains the specified tag
     */
    boolean containsTag(Handle tag);

    /**
     * @param id id of a tag to be checked for
     * @return {@code true} if this handle contains a tag with the specified id
     */
    boolean containsTagById(String id);

    /**
     * @param tags tags to be checked for
     * @return {@code true} if this handle contains all the specified tags
     */
    boolean containsTags(Collection<Handle> tags);

    /**
     * @param ids ids for the tags to be checked for
     * @return {@code true} if this handle contains tags with all the specified ids
     */
    boolean containsTagsById(Collection<String> ids);

    /**
     * @param tag tag to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    boolean removeTag(Handle tag);

    /**
     * @param id id of a tag to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    boolean removeTagById(String id);
    
    /**
     * @param tags tags to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    boolean removeTags(Collection<Handle> tags);

    /**
     * @param ids ids of the tags to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    boolean removeTagsById(Collection<String> ids);

    /**
     * @return {@link Stream} of the tags present in this handle
     */
    Stream<Handle> tagStream();

    /**
     * Clears all tags from this handle.
     */
    void clearTags();

    /**
     * Compares this handle with the specified handle for order. If both handles are present in the same {@link Space},
     * they are compared with their {@link #getIndex()} value using {@link Long#compare(long, long)}. If the Spaces are
     * different, the same comparison is done with the identifying handles of these Spaces. Returns a negative  integer,
     * zero, or a positive integer as this handle is less than, equal to, or greater than the specified handle.
     *
     * @param other the handle to be compared
     * @return a negative integer, zero, or a positive integer as this handle is less than, equal to, or greater than
     * the specified handle
     */
    @Override
    default int compareTo(Handle other) {
        if(getSpace().equals(other.getSpace())) {
            return Long.compare(getIndex(), other.getIndex());
        }
        return Long.compare(getSpace().getHandle().getIndex(),
                other.getSpace().getHandle().getIndex());
    }
}
