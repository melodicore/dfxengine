package me.datafox.dfxengine.handles;

import lombok.*;
import me.datafox.dfxengine.collections.TreeHandleSet;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link Space}.
 *
 * @author datafox
 */
@Data
public final class SpaceImpl implements Space {
    @NonNull
    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private final Logger logger;

    @NonNull
    @EqualsAndHashCode.Exclude
    private final HandleManager handleManager;

    @NonNull
    @EqualsAndHashCode.Exclude
    private final Handle handle;

    @Getter(AccessLevel.NONE)
    private final String handleId;

    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private final HandleSet handles;

    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private long counter = 0;

    @Builder
    private SpaceImpl(@NonNull Handle handle, @Singular Collection<String> initialHandles, @NonNull HandleManager handleManager) {
        this.handleManager = handleManager;
        logger = LoggerFactory.getLogger(SpaceImpl.class);
        this.handle = handle;
        handleId = handle.getId();
        handles = new TreeHandleSet(this);
        initialHandles.forEach(this::createHandle);
    }

    private SpaceImpl(@NonNull HandleManager handleManager, String id) {
        this.handleManager = handleManager;
        logger = LoggerFactory.getLogger(getClass());
        handleId = id;
        handles = new TreeHandleSet(this);
        handle = bootstrapHandle(id);
    }

    /**
     * @return {@link HandleManager} associated with this space
     */
    @Override
    public HandleManager getHandleManager() {
        return handleManager;
    }

    /**
     * {@link Handle} identifying this space, present in a hardcoded space, details of which are documented in
     * {@link HandleManager}.
     *
     * @return {@link Handle} identifying this space
     */
    @Override
    public Handle getHandle() {
        return handle;
    }

    /**
     * @return id of this space, and by definition its identifying {@link Handle}
     */
    @Override
    public String getId() {
        return handleId;
    }

    /**
     * @param handle {@link Handle} to be checked
     * @return {@code true} if the specified {@link Handle} matches the identifying Handle of this space
     */
    @Override
    public boolean isHandle(Handle handle) {
        return this.handle.equals(handle);
    }

    /**
     * @param id id to be checked
     * @return {@code true} if the specified id matches the id of this space
     */
    @Override
    public boolean isId(String id) {
        return handle.isId(id);
    }

    /**
     * @param id id of the requested {@link Handle}
     * @return contained {@link Handle} matching the specified id, or {@code null} if none are present
     */
    @Override
    public Handle getHandle(String id) {
        return handles.get(id);
    }

    /**
     * @return all {@link Handle Handles} present in this space
     */
    @Override
    public Collection<Handle> getHandles() {
        return Collections.unmodifiableSet(handles);
    }

    /**
     * @param tag tag to be checked for
     * @return all {@link Handle Handles} present in this space that contain the specified tag
     */
    @Override
    public Collection<Handle> getHandlesByTag(Handle tag) {
        return handleStream()
                .filter(handle -> handle.containsTag(tag))
                .collect(Collectors.toSet());
    }

    /**
     * @param id id of a tag to be checked for
     * @return all {@link Handle Handles} present in this space that contain the specified tag
     */
    @Override
    public Collection<Handle> getHandlesByTagId(String id) {
        return handleStream()
                .filter(handle -> handle.containsTagById(id))
                .collect(Collectors.toSet());
    }

    /**
     * @param tags tags to be checked for
     * @return all {@link Handle Handles} present in this space that contain all the specified tags
     */
    @Override
    public Collection<Handle> getHandlesByTags(Collection<Handle> tags) {
        return handleStream()
                .filter(handle -> handle.containsTags(tags))
                .collect(Collectors.toSet());
    }

    /**
     * @param ids tags to be checked for
     * @return all {@link Handle Handles} present in this space that contain tags with all the specified ids
     */
    @Override
    public Collection<Handle> getHandlesByTagIds(Collection<String> ids) {
        return handleStream()
                .filter(handle -> handle.containsTagsById(ids))
                .collect(Collectors.toSet());
    }

    /**
     * @param id id of the {@link Handle} to be created
     * @return created {@link Handle}
     *
     * @throws IllegalArgumentException if a {@link Handle} with the specified id is already present
     */
    @Override
    public Handle createHandle(String id) {
        if(handles.containsById(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.handleWithIdAlreadyPresent(this, id),
                    IllegalArgumentException::new);
        }

        Handle handle = HandleImpl
                .builder()
                .id(id)
                .space(this)
                .index(counter++)
                .build();

        handles.add(handle);

        return handle;
    }

    /**
     * Checks if a {@link Handle} is present with the specified id. If one is present, that Handle is returned. If none
     * are present, a new Handle with the specified id is created and returned.
     *
     * @param id id of the requested {@link Handle}
     * @return requested {@link Handle}
     */
    @Override
    public Handle getOrCreateHandle(String id) {
        if(containsHandleById(id)) {
            return getHandle(id);
        }
        return createHandle(id);
    }

    /**
     * @param handle {@link Handle} to be checked for
     * @return {@code true} if this space contains the specified {@link Handle}
     */
    @Override
    public boolean containsHandle(Handle handle) {
        return handles.contains(handle);
    }

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return {@code true} if this space contains a {@link Handle} with the specified id
     */
    @Override
    public boolean containsHandleById(String id) {
        return handles.containsById(id);
    }

    /**
     * @param handles {@link Handle Handles} to be checked for
     * @return {@code true} if this space contains all the specified {@link Handle Handles}
     */
    @Override
    public boolean containsHandles(Collection<Handle> handles) {
        return this.handles.containsAll(handles);
    }

    /**
     * @param ids {@link Handle} ids to be checked for
     * @return {@code true} if this space contains {@link Handle Handles} with all the specified ids
     */
    @Override
    public boolean containsHandlesById(Collection<String> ids) {
        return handles.containsAllById(ids);
    }

    /**
     * @param handle {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    @Override
    public boolean removeHandle(Handle handle) {
        return handles.remove(handle);
    }

    /**
     * @param id ids of the {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    @Override
    public boolean removeHandleById(String id) {
        return handles.removeById(id);
    }

    /**
     * @param handles {@link Handle Handles} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    @Override
    public boolean removeHandles(Collection<Handle> handles) {
        return this.handles.removeAll(handles);
    }

    /**
     * @param ids ids of the {@link Handle} to be removed
     * @return {@code true} if the {@link Handle Handles} of this space changed as a result of this action
     */
    @Override
    public boolean removeHandlesById(Collection<String> ids) {
        return handles.removeAllById(ids);
    }

    /**
     * @return {@link Stream} of the {@link Handle Handles} present in this space
     */
    @Override
    public Stream<Handle> handleStream() {
        return getHandles().stream();
    }

    /**
     * Clears all {@link Handle Handles} from this space.
     */
    @Override
    public void clear() {
        handles.clear();
    }

    /**
     * @return {@link String} representation of this space in format <i>spaceId[handleId1,handleId2...]</i> where
     * handleIds are the ids of the {@link Handle Handles} present within this space
     */
    @Override
    public String toString() {
        return String.format("%s[%s]", handleId, handles.stream().map(Handle::getId).collect(Collectors.joining(", ")));
    }

    static Space bootstrap(HandleManager handleManager, String id) {
        return new SpaceImpl(handleManager, id);
    }

    void addInternal(Handle handle) {
        handles.add(handle);
    }

    Handle bootstrapHandle(String id) {
        if(handles.containsById(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.handleWithIdAlreadyPresent(this, id),
                    IllegalArgumentException::new);
        }

        Handle handle = new HandleImpl(this, id, counter++);

        handles.add(handle);

        return handle;
    }
}
