package me.datafox.dfxengine.handles.api;

/**
 * A namespace for {@link Handle Handles}. May contain {@link Group Groups} to further categorise handles.
 *
 * @author datafox
 */
public interface Space extends Handled {
    /**
     * @return {@link HandleManager} managing this space
     */
    HandleManager getHandleManager();

    /**
     * @return unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space
     */
    HandleSet getHandles();

    /**
     * @param id {@link String} id for the new {@link Handle}
     * @return created {@link Handle}
     * @throws IllegalArgumentException if the id is {@code null}, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a {@link Handle} with the given id already exists in this space
     */
    Handle createHandle(String id);

    /**
     * Creates a {@link Handle} with the specified id if it does not already exist and returns the handle with that id.
     *
     * @param id {@link String} id for the {@link Handle}
     * @return created or pre-existing {@link Handle}
     * @throws IllegalArgumentException if the id is {@code null}, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
     */
    Handle getOrCreateHandle(String id);

    /**
     * @return unmodifiable {@link HandleMap} containing the {@link Group Groups} of this space
     */
    HandleMap<Group> getGroups();

    /**
     * @param id {@link String} id for the new {@link Group}
     * @return created {@link Group}
     * @throws IllegalArgumentException if the id is {@code null}, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a {@link Group} with the given id already exists in this space
     */
    Group createGroup(String id);

    /**
     * Creates a {@link Group} with the specified id if it does not already exist and returns the group with that id.
     *
     * @param id {@link String} id for the {@link Group}
     * @return created or pre-existing {@link Group}
     * @throws IllegalArgumentException if the id is {@code null}, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
     */
    Group getOrCreateGroup(String id);
}
