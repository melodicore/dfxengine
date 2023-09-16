package me.datafox.dfxengine.handles;

import lombok.AccessLevel;
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

import static me.datafox.dfxengine.handles.HandleConstants.SPACES_ID;
import static me.datafox.dfxengine.handles.HandleConstants.TAGS_ID;

/**
 * Implementation of {@link HandleManager}.
 *
 * @author datafox
 */
@Component(defaultFor = HandleManager.class)
@EqualsAndHashCode
@ToString
public class HandleManagerImpl implements HandleManager {
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final Logger logger;

    private final HandleMap<Space> spaces;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final Space spaceSpace;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Getter(AccessLevel.PACKAGE)
    private final Space tagSpace;

    @Inject
    public HandleManagerImpl(Logger logger) {
        this.logger = logger;
        spaceSpace = SpaceImpl.bootstrap(this, SPACES_ID);
        spaces = new TreeHandleMap<>(spaceSpace);
        tagSpace = SpaceImpl.builder()
                .handle(((SpaceImpl) spaceSpace).bootstrapHandle(TAGS_ID))
                .handleManager(this)
                .build();
        ((HandleImpl) spaceSpace.getHandle()).bootstrap(tagSpace);
        ((HandleImpl) tagSpace.getHandle()).bootstrap(tagSpace);
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
    public Handle getSpaceHandle(String id) {
        return spaceSpace.getHandle(id);
    }

    @Override
    public Collection<Space> getSpaces() {
        return spaces.values();
    }

    @Override
    public Collection<Handle> getSpaceHandles() {
        return spaceSpace.getHandles();
    }

    @Override
    public Collection<Handle> getTags() {
        return tagSpace.getHandles();
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
    public boolean containsSpaces(Collection<Space> spaces) {
        return spaces.stream().allMatch(this.spaces::containsValue);
    }

    @Override
    public boolean containsSpacesByHandle(Collection<Handle> handles) {
        return spaces.containsAll(handles);
    }

    @Override
    public boolean containsSpacesById(Collection<String> ids) {
        return spaces.containsAllById(ids);
    }

    @Override
    public boolean removeSpace(Space space) {
        checkHardcoded(space);

        spaceSpace.removeHandle(space.getHandle());

        return spaces.remove(space.getHandle(), space);
    }

    @Override
    public boolean removeSpaceByHandle(Handle handle) {
        checkHardcoded(handle);

        spaceSpace.removeHandle(handle);

        return spaces.remove(handle) != null;
    }

    @Override
    public boolean removeSpaceById(String id) {
        checkHardcoded(id);

        spaceSpace.removeHandleById(id);

        return spaces.removeById(id) != null;
    }

    @Override
    public boolean removeSpaces(Collection<Space> spaces) {
        spaces.forEach(this::checkHardcoded);

        return removeSpacesByHandle(spaces
                .stream()
                .map(Space::getHandle)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean removeSpacesByHandle(Collection<Handle> handles) {
        handles.forEach(this::checkHardcoded);

        spaceSpace.removeHandles(handles);

        return spaces.removeAll(handles);
    }

    @Override
    public boolean removeSpacesById(Collection<String> ids) {
        ids.forEach(this::checkHardcoded);

        spaceSpace.removeHandlesById(ids);

        return spaces.removeAllById(ids);
    }

    @Override
    public Stream<Space> spaceStream() {
        return spaces.stream();
    }

    @Override
    public Handle getTag(String id) {
        return tagSpace.getHandle(id);
    }

    @Override
    public Handle createTag(String id) {
        return tagSpace.createHandle(id);
    }

    @Override
    public Handle getOrCreateTag(String id) {
        return tagSpace.getOrCreateHandle(id);
    }

    @Override
    public boolean containsTag(Handle tag) {
        return tagSpace.containsHandle(tag);
    }

    @Override
    public boolean containsTagById(String id) {
        return tagSpace.containsHandleById(id);
    }

    @Override
    public boolean containsTags(Collection<Handle> tags) {
        return tagSpace.containsHandles(tags);
    }

    @Override
    public boolean containsTagsById(Collection<String> ids) {
        return tagSpace.containsHandlesById(ids);
    }

    @Override
    public boolean removeTag(Handle tag) {
        return tagSpace.removeHandle(tag);
    }

    @Override
    public boolean removeTagById(String id) {
        return tagSpace.removeHandleById(id);
    }

    @Override
    public boolean removeTags(Collection<Handle> tags) {
        return tagSpace.removeHandles(tags);
    }

    @Override
    public boolean removeTagsById(Collection<String> ids) {
        return tagSpace.removeHandlesById(ids);
    }

    @Override
    public Stream<Handle> tagStream() {
        return tagSpace.handleStream();
    }

    @Override
    public void clear() {
        tagSpace.clear();
        spaceSpace.clear();
        ((SpaceImpl) spaceSpace).addInternal(spaceSpace.getHandle());
        ((SpaceImpl) spaceSpace).addInternal(tagSpace.getHandle());
        spaces.clear();
        spaces.put(spaceSpace.getHandle(), spaceSpace);
        spaces.put(tagSpace.getHandle(), tagSpace);
    }

    private void checkHardcoded(Space space) {
        if(space.equals(spaceSpace) || space.equals(tagSpace)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.removeHardcodedSpace(space),
                    IllegalArgumentException::new);
        }
    }

    private void checkHardcoded(Handle handle) {
        if(spaceSpace.isHandle(handle) || tagSpace.isHandle(handle)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.removeHardcodedSpace(getSpace(handle)),
                    IllegalArgumentException::new);
        }
    }

    private void checkHardcoded(String id) {
        if(spaceSpace.isId(id) || tagSpace.isId(id)) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.removeHardcodedSpace(getSpaceById(id)),
                    IllegalArgumentException::new);
        }
    }
}
