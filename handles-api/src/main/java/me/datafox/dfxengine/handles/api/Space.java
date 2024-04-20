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
     * @return unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, excluding subhandles
     */
    HandleSet getHandles();

    /**
     * Returns an unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, including
     * subhandles. Please note that unlike {@link #getHandles()}, the handle set returned by this method does not
     * reflect handles created after calling this method.
     *
     * @return unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, including subhandles
     */
    HandleSet getAllHandles();

    /**
     * Creates a new {@link Handle}. Throws {@link UnsupportedOperationException} if this is the space containing
     * handles of spaces ({@link HandleManager#getSpaceSpace()}, use {@link HandleManager#createSpace(String)} instead).
     *
     * @param id {@link String} id for the new {@link Handle}
     * @return created {@link Handle}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or
     * more than one colon ({@code :}), or if a {@link Handle} with the given id already exists in this space
     * @throws UnsupportedOperationException if this is the space containing {@link Handle Handles} of spaces
     */
    Handle createHandle(String id);

    /**
     * Creates a {@link Handle} with the specified id if it does not already exist and returns the handle with that id.
     * Throws {@link UnsupportedOperationException} if this is the space containing handles of spaces
     * ({@link HandleManager#getSpaceSpace()}) and a handle with the specified id is not present. Use
     * {@link HandleManager#getOrCreateSpace(String)} instead.
     *
     * @param id {@link String} id for the {@link Handle}
     * @return created or pre-existing {@link Handle}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or
     * more than one colon ({@code :})
     * @throws UnsupportedOperationException if this is the space containing {@link Handle Handles} of spaces and a
     * handle with the specified id is not present
     */
    Handle getOrCreateHandle(String id);

    /**
     * @return unmodifiable {@link HandleMap} containing the {@link Group Groups} of this space
     */
    HandleMap<Group> getGroups();

    /**
     * @param id {@link String} id for the new {@link Group}
     * @return created {@link Group}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a {@link Group} with the given id already exists in this space
     */
    Group createGroup(String id);

    /**
     * Creates a {@link Group} with the specified id if it does not already exist and returns the group with that id.
     *
     * @param id {@link String} id for the {@link Group}
     * @return created or pre-existing {@link Group}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
     */
    Group getOrCreateGroup(String id);
}
