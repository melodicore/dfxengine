package me.datafox.dfxengine.handles;

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
public class HandleManagerImpl implements HandleManager {
    private final Logger logger;

    private final HandleMap<Space> spaces;

    @Inject
    public HandleManagerImpl(Logger logger) {
        this.logger = logger;
        spaces = new TreeHandleMap<>(HandleConstants.SPACES,
                HandleConstants.SPACES_SET.stream()
                .collect(Collectors.toMap(
                        Space::getHandle,
                        space -> space)));
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
        Handle handle = HandleConstants.SPACES.getOrCreateHandle(id);

        if(spaces.containsById(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.spaceWithIdAlreadyPresent(id),
                    IllegalArgumentException::new);
        }

        Space space = SpaceImpl
                .builder()
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
        HandleConstants.SPACES.removeHandle(space.getHandle());

        return spaces.remove(space.getHandle(), space);
    }

    @Override
    public Space removeSpaceByHandle(Handle handle) {
        HandleConstants.SPACES.removeHandle(handle);

        return spaces.remove(handle);
    }

    @Override
    public Space removeSpaceById(String id) {
        HandleConstants.SPACES.removeHandleById(id);

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
        HandleConstants.SPACES.removeHandles(handles);

        return spaces.removeAll(handles);
    }

    @Override
    public boolean removeSpacesById(Collection<String> ids) {
        HandleConstants.SPACES.removeHandlesById(ids);

        return spaces.removeAllById(ids);
    }

    @Override
    public Stream<Space> spaceStream() {
        return spaces.stream();
    }
}
