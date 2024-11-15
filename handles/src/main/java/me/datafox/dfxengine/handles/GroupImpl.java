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
    @EqualsAndHashCode.Include
    private final Handle handle;
    private final HandleManager handleManager;
    private final Space space;
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
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
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
