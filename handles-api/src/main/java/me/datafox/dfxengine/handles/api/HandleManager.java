package me.datafox.dfxengine.handles.api;

/**
 * A singleton class that manages {@link Space Spaces} and {@link Handle Handles}. Contains two hardcoded spaces, one
 * for the handles used to identify spaces themselves, and one for tag handles.
 *
 * @author datafox
 */
public interface HandleManager {
    /**
     * @return {@link Space} containing {@link Handle Handles} used for identifying spaces
     */
    Space getSpaceSpace();

    /**
     * @return {@link Space} containing tag {@link Handle Handles}
     */
    Space getTagSpace();

    /**
     * @return unmodifiable {@link HandleMap} containing all {@link Space Spaces} managed by this handle manager
     */
    HandleMap<Space> getSpaces();

    /**
     * @param id {@link String} id for the new {@link Space}
     * @return created {@link Space}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a {@link Space} with the given id already exists
     */
    Space createSpace(String id);

    /**
     * Creates a {@link Space} with the specified id if it does not already exist and returns the space with that id.
     *
     * @param id {@link String} id for the {@link Space}
     * @return created or pre-existing {@link Space}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
     */
    Space getOrCreateSpace(String id);
}
