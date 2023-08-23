package me.datafox.dfxengine.handles;

import lombok.*;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.collection.TreeHandleSet;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
public final class SpaceImpl implements Space {
    @Getter(AccessLevel.NONE)
    private final Logger logger;

    @NonNull
    private final Handle handle;

    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private final HandleSet handles;

    @Getter(AccessLevel.NONE)
    private long counter = 0;

    @Builder
    private SpaceImpl(@NonNull Handle handle, @Singular Collection<String> initialHandles) {
        logger = LoggerFactory.getLogger(getClass());
        this.handle = handle;
        handles = new TreeHandleSet(this);
        initialHandles.forEach(this::createHandle);
    }

    private SpaceImpl(String spacesId) {
        logger = LoggerFactory.getLogger(getClass());
        handle = HandleImpl
                .builder()
                .id(spacesId)
                .space(this)
                .index(counter++)
                .build();

        handles = new TreeHandleSet(this);
        handles.add(handle);
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

    static Space bootstrap(String spacesId) {
        return new SpaceImpl(spacesId);
    }
}
