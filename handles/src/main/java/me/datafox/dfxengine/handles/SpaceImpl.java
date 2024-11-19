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
    /**
     * {@link Logger} of this space.
     */
    private final Logger logger;

    /**
     * Identifying {@link Handle} of this space.
     */
    @EqualsAndHashCode.Include
    private final Handle handle;

    /**
     * {@link HandleManager} managing this space.
     */
    private final HandleManager handleManager;

    private final HandleSet handles;
    private final HandleMap<Group> groups;

    /**
     * Package-private constructor for {@link SpaceImpl}.
     *
     * @param handle {@link Handle} for identifying this space
     * @param handleManager {@link HandleManager} managing this space
     */
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
     * Returns an unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, excluding
     * subhandles.
     *
     * @return unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, excluding subhandles
     */
    @Override
    public HandleSet getHandles() {
        return handles.unmodifiable();
    }

    /**
     * Returns an unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, including
     * subhandles. Please note that unlike {@link #getHandles()}, the handle set returned by this method does not
     * reflect handles created after calling this method.
     *
     * @return unmodifiable {@link HandleSet} containing the {@link Handle Handles} of this space, including subhandles
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
    @Override
    public Handle getOrCreateHandle(String id) {
        checkId(HANDLE, id, logger);
        if(handles.contains(id)) {
            return handles.get(id);
        }
        return createHandleInternal(id);
    }

    /**
     * Returns an unmodifiable {@link HandleMap} containing the {@link Group Groups} of this space.
     *
     * @return unmodifiable {@link HandleMap} containing the {@link Group Groups} of this space
     */
    @Override
    public HandleMap<Group> getGroups() {
        return groups.unmodifiable();
    }

    /**
     * Creates a {@link Group} with the specified id.
     *
     * @param id {@link String} id for the new {@link Group}
     * @return created {@link Group}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :}), or if a {@link Group} with the given id already exists in this space
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
     * Creates a {@link Group} with the specified id if it does not already exist and returns the group with that id.
     *
     * @param id {@link String} id for the {@link Group}
     * @return created or pre-existing {@link Group}
     * @throws NullPointerException if the id is {@code null}
     * @throws IllegalArgumentException if the id is empty, blank, contains non-ASCII or non-printable characters or the
     * colon ({@code :})
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

    /**
     * Returns the {@link String} representation of this space.
     *
     * @return {@link String} representation of this space
     */
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
