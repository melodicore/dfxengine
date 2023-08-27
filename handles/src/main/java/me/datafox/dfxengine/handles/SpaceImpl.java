package me.datafox.dfxengine.handles;

import lombok.*;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.collection.TreeHandleSet;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
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
        logger = LoggerFactory.getLogger(getClass());
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

    @Override
    public String getId() {
        return handleId;
    }

    @Override
    public boolean isHandle(Handle handle) {
        return this.handle.equals(handle);
    }

    @Override
    public boolean isId(String id) {
        return handle.isId(id);
    }

    @Override
    public Handle getHandle(String id) {
        return handles.get(id);
    }

    @Override
    public Collection<Handle> getHandles() {
        return Collections.unmodifiableSet(handles);
    }

    @Override
    public Collection<Handle> getHandlesByTag(Handle tag) {
        return handleStream()
                .filter(handle -> handle.containsTag(tag))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Handle> getHandlesByTagId(String id) {
        return handleStream()
                .filter(handle -> handle.containsTagById(id))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Handle> getHandlesByTags(Collection<Handle> tags) {
        return handleStream()
                .filter(handle -> handle.containsTags(tags))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Handle> getHandlesByTagIds(Collection<String> ids) {
        return handleStream()
                .filter(handle -> handle.containsTagsById(ids))
                .collect(Collectors.toSet());
    }

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

    @Override
    public Handle getOrCreateHandle(String id) {
        if(containsHandleById(id)) {
            return getHandle(id);
        }
        return createHandle(id);
    }

    @Override
    public boolean containsHandle(Handle handle) {
        return handles.contains(handle);
    }

    @Override
    public boolean containsHandleById(String id) {
        return handles.containsById(id);
    }

    @Override
    public boolean containsHandles(Collection<Handle> handles) {
        return this.handles.containsAll(handles);
    }

    @Override
    public boolean containsHandlesById(Collection<String> ids) {
        return handles.containsAllById(ids);
    }

    @Override
    public boolean removeHandle(Handle handle) {
        return handles.remove(handle);
    }

    @Override
    public boolean removeHandleById(String id) {
        return handles.removeById(id);
    }

    @Override
    public boolean removeHandles(Collection<Handle> handles) {
        return this.handles.removeAll(handles);
    }

    @Override
    public boolean removeHandlesById(Collection<String> ids) {
        return handles.removeAllById(ids);
    }

    @Override
    public Stream<Handle> handleStream() {
        return getHandles().stream();
    }

    @Override
    public void clear() {
        handles.clear();
    }

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
