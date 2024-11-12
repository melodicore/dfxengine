package me.datafox.dfxengine.handles.api;

/**
 * A container of {@link Handle Handles}. May only contain handles from its associated {@link Space}.
 *
 * @author datafox
 */
public interface Group extends Handled {
    /**
     * Returns the {@link HandleManager} managing this group.
     *
     * @return {@link HandleManager} managing this group
     */
    HandleManager getHandleManager();

    /**
     * Returns the {@link Space} associated with this group.
     *
     * @return {@link Space} associated with this group
     */
    Space getSpace();

    /**
     * Returns the {@link HandleSet} containing the {@link Handle Handles} of this group. The returned set is modifiable
     * and handles may be added to or removed from it at will.
     *
     * @return {@link HandleSet} containing the {@link Handle Handles} of this group
     */
    HandleSet getHandles();
}
