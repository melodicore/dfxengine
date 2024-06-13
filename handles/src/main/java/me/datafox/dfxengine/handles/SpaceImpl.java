package me.datafox.dfxengine.handles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.datafox.dfxengine.handles.api.*;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.datafox.dfxengine.handles.utils.HandleStrings.*;
import static me.datafox.dfxengine.handles.utils.HandleUtils.checkId;

/**
 * Implementation of {@link Space}.
 *
 * @author datafox
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpaceImpl implements Space {
    private final Logger logger;
    @EqualsAndHashCode.Include
    private final Handle handle;
    private final HandleManager handleManager;
    private final HandleSet handles;
    private final HandleMap<Group> groups;

    SpaceImpl(Handle handle, HandleManager handleManager) {
        this.handle = handle;
        this.handleManager = handleManager;
        logger = ((HandleManagerImpl) handleManager).getLogger();
        HandleManagerConfiguration conf = ((HandleManagerImpl) handleManager).getConfiguration();
        handles = conf.isOrderedHandlesInSpaces() ?
                new TreeHandleSet(this, logger) :
                new HashHandleSet(this, logger);
        groups = conf.isOrderedGroups() ?
                new TreeHandleMap<>(handleManager.getSpaceSpace(), logger) :
                new HashHandleMap<>(handleManager.getSpaceSpace(), logger);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public HandleSet getHandles() {
        return handles.unmodifiable();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public HandleSet getAllHandles() {
        return handles
                .stream()
                .flatMap(handle ->
                        Stream.concat(Stream.of(handle),handle.getSubHandles().stream()))
                .collect(Collectors.toCollection(() ->
                        ((HandleManagerImpl) handleManager)
                                .getConfiguration()
                                .isOrderedHandlesInSpaces() ?
                                new TreeHandleSet(SpaceImpl.this, logger) :
                                new HashHandleSet(SpaceImpl.this, logger)))
                .unmodifiable();
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    @Override
    public Handle createHandle(String id) {
        checkId(HANDLE, id, logger);
        if(handles.contains(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    unavailableId(HANDLE, id),
                    IllegalArgumentException::new);
        }
        return createHandleInternal(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    @Override
    public Handle getOrCreateHandle(String id) {
        checkId(HANDLE, id, logger);
        if(handles.contains(id)) {
            return handles.get(id);
        }
        return createHandleInternal(id);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public HandleMap<Group> getGroups() {
        return groups.unmodifiable();
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@link String} id for the new {@link Group}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Group createGroup(String id) {
        checkId(GROUP, id, logger);
        checkGroupId(id);
        String properId = handle.getId() + ":" + id;
        if(groups.containsKey(properId)) {
            throw LogUtils.logExceptionAndGet(logger,
                    unavailableId(GROUP, id),
                    IllegalArgumentException::new);
        }
        return createGroupInternal(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param id {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override
    public Group getOrCreateGroup(String id) {
        checkId(GROUP, id, logger);
        checkGroupId(id);
        String properId = handle.getId() + ":" + id;
        if(groups.containsKey(properId)) {
            return groups.get(properId);
        }
        return createGroupInternal(id);
    }

    @Override
    public String toString() {
        return String.format("Space(%s, [%s])",
                handle.getId(),
                handles.stream()
                        .map(Handle::getId)
                        .collect(Collectors.joining(", ")));
    }

    private void checkGroupId(String id) {
        if(id.contains(":")) {
            throw LogUtils.logExceptionAndGet(logger,
                    GROUP_ID,
                    IllegalArgumentException::new);
        }
    }

    private Handle createHandleInternal(String id) {
        Handle handle;
        if(id.contains(":")) {
            String[] split = id.split(":");
            Handle parent = getOrCreateHandle(split[0]);
            handle = parent.getOrCreateSubHandle(split[1]);
        } else {
            handle = new HandleImpl(this, id, handles.size(), -1);
        }
        handles.add(handle);
        return handle;
    }

    private Group createGroupInternal(String id) {
        Handle handle = ((HandleManagerImpl.SpaceHandle) getHandle()).createSubHandleInternal(id);
        Group group = new GroupImpl(handle, this);
        groups.putHandled(group);
        return group;
    }
}
