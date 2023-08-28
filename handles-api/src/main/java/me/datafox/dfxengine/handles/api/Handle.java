package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * An identifier class with a tagging system. A Handle in its most bare-bones understanding contains a {@link String}
 * id, a {@link Space} which is a namespace for ids, and zero or more tags, which are also Handles. In practice a Handle
 * also contains an index used for ordering, and a reference to a {@link HandleManager} which is used to manipulate
 * Handles and Spaces, including tags. Handles are primarily designed to be used as map keys and for other identifying
 * purposes.
 *
 * @author datafox
 */
public interface Handle extends Comparable<Handle> {
    /**
     * @return {@link HandleManager} associated with this Handle
     */
    HandleManager getHandleManager();

    /**
     * @return {@link Space} containing this Handle
     */
    Space getSpace();

    /**
     * @return {@link String} id of this Handle
     */
    String getId();

    /**
     * The index value used for ordering Handles within a {@link Space}.
     *
     * @return index of this Handle
     */
    long getIndex();

    /**
     * @param id id to be checked for
     * @return true if the specified id matches the id of this Handle
     */
    boolean isId(String id);

    /**
     * @return all tags associated with this Handle
     */
    Collection<Handle> getTags();

    /**
     * @param tag Handle to be added as a tag
     * @return true if the tags of this Handle changed as a result of this action
     *
     * @throws IllegalArgumentException when the specified Handle is not a part of the tags {@link Space} (details for
     * hardcoded Spaces are documented in {@link HandleManager})
     */
    boolean addTag(Handle tag);

    /**
     * Adds a tag based on the specified id for the tag. If a tag with the specified id does not exist in the tags {@link Space},
     * one is automatically created. Details for hardcoded Spaces are documented in {@link HandleManager}.
     *
     * @param id id of the Handle to be added as a tag
     * @return true if the tags of this Handle changed as a result of this action
     */
    boolean addTagById(String id);


    /**
     * @param tags Handles to be added as a tags
     * @return true if the tags of this Handle changed as a result of this action
     *
     * @throws IllegalArgumentException when any of the specified Handles are not a part of the tags {@link Space} (details
     * for hardcoded Spaces are documented in {@link HandleManager})
     */
    boolean addTags(Collection<Handle> tags);

    /**
     * Adds tags based on the specified ids for the tags. If a tag with the specified id does not exist in the tags {@link Space}, one
     * is automatically created. Details for hardcoded Spaces are documented in {@link HandleManager}.
     *
     * @param ids ids for the Handles to be added as a tags
     * @return true if the tags of this Handle changed as a result of this action
     */
    boolean addTagsById(Collection<String> ids);

    /**
     * @param tag tag to be checked for
     * @return true if this Handle contains the specified tag
     */
    boolean containsTag(Handle tag);

    /**
     * @param id id of a tag to be checked for
     * @return true if this Handle contains a tag with the specified id
     */
    boolean containsTagById(String id);

    /**
     * @param tags tags to be checked for
     * @return true if this Handle contains all the specified tags
     */
    boolean containsTags(Collection<Handle> tags);

    /**
     * @param ids ids for the tags to be checked for
     * @return true if this Handle contains tags with all the specified ids
     */
    boolean containsTagsById(Collection<String> ids);

    /**
     * @param tag tag to be removed
     * @return true if the tags of this Handle changed as a result of this action
     */
    boolean removeTag(Handle tag);

    /**
     * @param id id of a tag to be removed
     * @return true if the tags of this Handle changed as a result of this action
     */
    boolean removeTagById(String id);
    
    /**
     * @param tags tags to be removed
     * @return true if the tags of this Handle changed as a result of this action
     */
    boolean removeTags(Collection<Handle> tags);

    /**
     * @param ids ids of the tags to be removed
     * @return true if the tags of this Handle changed as a result of this action
     */
    boolean removeTagsById(Collection<String> ids);

    /**
     * @return {@link Stream} of the tags present in this Handle
     */
    Stream<Handle> tagStream();

    /**
     * Clears all tags from this Handle.
     */
    void clearTags();

    @Override
    default int compareTo(Handle o) {
        if(getSpace().equals(o.getSpace())) {
            return Long.compare(getIndex(), o.getIndex());
        }
        return Long.compare(getSpace().getHandle().getIndex(),
                o.getSpace().getHandle().getIndex());
    }
}
