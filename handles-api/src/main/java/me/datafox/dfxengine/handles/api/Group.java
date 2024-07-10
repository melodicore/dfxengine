package me.datafox.dfxengine.handles.api;

/**
 * A container of {@link Handle Handles}. May only contain handles from its associated {@link Space}.
 *
 * @author datafox
 */
public interface Group extends Handled {
    /**
     * @return {@link HandleManager} managing this group
     */
    HandleManager getHandleManager();

    /**
     * @return {@link Space} associated with this group
     */
    Space getSpace();

    /**
     * The returned {@link HandleSet} is modifiable and {@link Handle Handles} may be added to or removed from it at
     * will.
     *
     * @return {@link HandleSet} containing the {@link Handle Handles} of this group
     */
    HandleSet getHandles();
}
