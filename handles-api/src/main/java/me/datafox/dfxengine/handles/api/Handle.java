package me.datafox.dfxengine.handles.api;

/**
 * <p>
 * A handle is an identifier used for various objects. It contains a {@link String} id, an integer index and an
 * arbitrary number of tags, which are also handles. A handle is always contained in a single {@link Space}, and may
 * belong to one or more {@link Group Groups} within that Space. A handle's id may consist of printable ASCII
 * characters, with the exception of the colon ({@code :}) which is reserved as a separator for subhandles.
 * </p>
 * <p>
 * A handle may have any number of subhandles. The ids of subhandles are in the format {@code handle:subhandle}. A
 * subhandle may not have subhandles of its own. Subhandles may be used in any situation where a handle would need to
 * have parameters attached to it that are also handles. Subhandles are used internally for the identifying handles of
 * {@link Space Spaces} to identify their respective {@link Group Groups}.
 * </p>
 * <p>
 * Every handle has an index and a sub-index. Indices are unique for handles within a given {@link Space}, starting from
 * 0. Sub-indices are unique for subhandles within their parent handle, also starting from 0. A handle that is not a
 * subhandle has a sub-index of -1.
 * </p>
 *
 * @author datafox
 */
public interface Handle extends Comparable<Handle> {
    /**
     * @return {@link HandleManager} managing this handle
     */
    HandleManager getHandleManager();

    /**
     * @return {@link Space} that this handle is contained in
     */
    Space getSpace();

    /**
     * @return {@link String} id of this handle
     */
    String getId();

    /**
     * @return index of this handle
     */
    int getIndex();

    /**
     * Returns {@code -1} if this handle is not a subhandle.
     *
     * @return sub-index of this handle
     */
    int getSubIndex();

    /**
     * @return {@link Group Groups} containing this handle
     */
    HandleMap<Group> getGroups();

    /**
     * @return subhandles contained in this handle
     */
    HandleSet getSubHandles();

    /**
     * @param id {@link String} id for the new subhandle
     * @return created subhandle
     * @throws IllegalArgumentException if the id is {@code null}, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a subhandle with the given id already exists in this handle
     */
    Handle createSubHandle(String id);

    /**
     * Creates a subhandle with the specified id if it does not already exist and returns the subhandle with that id.
     *
     * @param id {@link String} id for the subhandle
     * @return created or pre-existing subhandle
     * @throws IllegalArgumentException if the id is {@code null}, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
     */
    Handle getOrCreateSubHandle(String id);

    /**
     * The returned {@link HandleSet} is modifiable and tags may be added to or removed from it at will.
     *
     * @return {@link HandleSet} containing the tags of this handle
     */
    HandleSet getTags();

    /**
     * @return {@code true} if this handle is a subhandle
     */
    boolean isSubHandle();

    /**
     * Compares this handle with the specified handle for order. Returns a negative integer, zero, or a positive integer
     * as this handle is less than, equal to, or greater than the specified handle. The order of handles is specified by
     * three things. If the {@link Space Spaces} of the handles are different, comparison is done with the space's
     * handle. If the spaces are the same, comparison is done with the index, using the sub-index if the indices are
     * also the same.
     *
     * @param o handle to be compared.
     * @return negative integer, zero, or a positive integer as this handle is less than, equal to, or greater than the
     * specified handle
     * @throws NullPointerException if the specified handle is {@code null}
     */
    @Override
    int compareTo(Handle o);
}
