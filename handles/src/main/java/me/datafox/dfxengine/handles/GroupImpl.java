package me.datafox.dfxengine.handles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.*;
import org.slf4j.Logger;

import java.util.stream.Collectors;

/**
 * Implementation of {@link Group}.
 *
 * @author datafox
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupImpl implements Group {
    /**
     * Identifying {@link Handle} of this group.
     */
    @EqualsAndHashCode.Include
    private final Handle handle;

    /**
     * {@link HandleManager} managing this group.
     */
    private final HandleManager handleManager;

    /**
     * {@link Space} associated with this group
     */
    private final Space space;

    /**
     * {@link HandleSet} containing the {@link Handle Handles} of this group.
     */
    private final HandleSet handles;

    /**
     * Package-private constructor for {@link GroupImpl}.
     *
     * @param handle {@link Handle} for identifying this group
     * @param space {@link Space} containing this group
     */
    GroupImpl(Handle handle, Space space) {
        this.handle = handle;
        handleManager = handle.getHandleManager();
        Logger logger = ((HandleManagerImpl) handleManager).getLogger();
        HandleManagerConfiguration conf = ((HandleManagerImpl) handleManager).getConfiguration();
        this.space = space;
        handles = conf.isOrderedHandlesInGroups() ?
                new TreeHandleSet(space, logger) :
                new HashHandleSet(space, logger);
    }

    /**
     * Returns the {@link String} representation of this group.
     *
     * @return {@link String} representation of this group
     */
    @Override
    public String toString() {
        return String.format("Group(%s, [%s])",
                space.getHandle().getId(),
                handles.stream()
                        .map(Handle::getId)
                        .collect(Collectors.joining(", ")));
    }
}
