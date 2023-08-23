package me.datafox.dfxengine.handles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.handles.collection.TreeHandleMap;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.injector.api.annotation.Component;
import me.datafox.dfxengine.injector.api.annotation.Inject;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Component
@EqualsAndHashCode
@ToString
public class HandleManagerImpl implements HandleManager {
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final Logger logger;

    private final HandleMap<Space> spaces;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Getter
    private final Space spaceSpace;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Getter
    private final Space tagSpace;

    @Inject
    public HandleManagerImpl(Logger logger) {
        this.logger = logger;
        spaceSpace = SpaceImpl.bootstrap(this, HandleConstants.SPACES_ID);
        spaces = new TreeHandleMap<>(spaceSpace);
        tagSpace = createSpace(HandleConstants.TAGS_ID);
        clear();
    }

    @Override
    public Space getSpace(Handle handle) {
        return spaces.get(handle);
    }

    @Override
    public Space getSpaceById(String id) {
        return spaces.getById(id);
    }

    @Override
    public Collection<Space> getSpaces() {
        return spaces.values();
    }

    @Override
    public Space createSpace(String id) {
        Handle handle = spaceSpace.getOrCreateHandle(id);

        if(spaces.containsById(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.spaceWithIdAlreadyPresent(id),
                    IllegalArgumentException::new);
        }

        Space space = SpaceImpl
                .builder()
                .handleManager(this)
                .handle(handle)
                .build();

        spaces.put(handle, space);

        return space;
    }

    @Override
    public Space getOrCreateSpace(String id) {
        if(containsSpaceById(id)) {
            return getSpaceById(id);
        }

        return createSpace(id);
    }

    @Override
    public boolean containsSpace(Space space) {
        return spaces.containsValue(space);
    }

    @Override
    public boolean containsSpaceByHandle(Handle handle) {
        return spaces.containsKey(handle);
    }

    @Override
    public boolean containsSpaceById(String id) {
        return spaces.containsById(id);
    }

    @Override
    public boolean removeSpace(Space space) {
        spaceSpace.removeHandle(space.getHandle());

        return spaces.remove(space.getHandle(), space);
    }

    @Override
    public Space removeSpaceByHandle(Handle handle) {
        spaceSpace.removeHandle(handle);

        return spaces.remove(handle);
    }

    @Override
    public Space removeSpaceById(String id) {
        spaceSpace.removeHandleById(id);

        return spaces.removeById(id);
    }

    @Override
    public boolean removeSpaces(Collection<Space> spaces) {
        return removeSpacesByHandle(spaces
                .stream()
                .map(Space::getHandle)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean removeSpacesByHandle(Collection<Handle> handles) {
        spaceSpace.removeHandles(handles);

        return spaces.removeAll(handles);
    }

    @Override
    public boolean removeSpacesById(Collection<String> ids) {
        spaceSpace.removeHandlesById(ids);

        return spaces.removeAllById(ids);
    }

    @Override
    public Stream<Space> spaceStream() {
        return spaces.stream();
    }

    @Override
    public void clear() {
        spaces.clear();
        spaces.put(spaceSpace.getHandle(), spaceSpace);
        spaces.put(tagSpace.getHandle(), tagSpace);
    }
}
