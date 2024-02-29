package me.datafox.dfxengine.handles;

import lombok.AccessLevel;
import lombok.Getter;
import me.datafox.dfxengine.collections.TreeHandleMap;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
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
 * <p>
 * A singleton class for managing {@link Handle Handles} and {@link Space Spaces}. Contains an ordered collection of
 * Spaces, order of which is determined by those Spaces' identifying Handles. Must define two special Spaces, one for
 * the Handles identifying Spaces, and one for the Handles used as tags.
 * </p>
 * <p>
 * This class is annotated with {@link Component} as a default implementation for {@link HandleManager}.
 * </p>
 * @author datafox
 */
@Component(order = Integer.MAX_VALUE)
public class HandleManagerImpl implements HandleManager {
    private final Logger logger;

    private final HandleMap<Space> spaces;

    private final Space spaceSpace;

    @Getter(AccessLevel.PACKAGE)
    private final Space tagSpace;

    /**
     * @param logger {@link Logger} for this handle manager
     */
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

    /**
     * @param id id of the requested {@link Handle}
     * @return {@link Space} identification {@link Handle} matching the specified id, or {@code null} if none are
     * present
     */
    @Override
    public Handle getSpaceHandle(String id) {
        return spaceSpace.getHandle(id);
    }

    /**
     * @return all identifying {@link Handle Handles} of the {@link Space Spaces} present in this handle manager
     */
    @Override
    public Collection<Handle> getSpaceHandles() {
        return spaceSpace.getHandles();
    }

    /**
     * @param handle {@link Handle} of the requested {@link Space}
     * @return contained {@link Space} matching the specified {@link Handle}, or {@code null} if none are present
     */
    @Override
    public Space getSpace(Handle handle) {
        return spaces.get(handle);
    }

    /**
     * @param id id of the requested {@link Space}
     * @return contained {@link Space} matching the specified id, or {@code null} if none are present
     */
    @Override
    public Space getSpaceById(String id) {
        return spaces.getById(id);
    }

    /**
     * @return all {@link Space Spaces} present in this handle manager
     */
    @Override
    public Collection<Space> getSpaces() {
        return spaces.values();
    }

    /**
     * @param id id of the {@link Space} to be created
     * @return created {@link Space}
     *
     * @throws IllegalArgumentException if a {@link Space} with the specified id is already present
     */
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

    /**
     * Checks if a {@link Space} is present with the specified id. If one is present, that Space is returned. If none
     * are present, a new Space with the specified id is created and returned.
     *
     * @param id id of the requested {@link Space}
     * @return requested {@link Space}
     */
    @Override
    public Space getOrCreateSpace(String id) {
        if(containsSpaceById(id)) {
            return getSpaceById(id);
        }

        return createSpace(id);
    }

    /**
     * @param space {@link Space} to be checked for
     * @return {@code true} if this handle manager contains the specified {@link Space}
     */
    @Override
    public boolean containsSpace(Space space) {
        return spaces.containsValue(space);
    }

    /**
     * @param handle identifying {@link Handle} of the {@link Space} to be checked for
     * @return {@code true} if this handle manager contains a {@link Space} with the specified {@link Handle}
     */
    @Override
    public boolean containsSpaceByHandle(Handle handle) {
        return spaces.containsKey(handle);
    }

    /**
     * @param id id of the {@link Space} to be checked for
     * @return {@code true} if this handle manager contains a {@link Space} with the specified id
     */
    @Override
    public boolean containsSpaceById(String id) {
        return spaces.containsById(id);
    }

    /**
     * @param spaces {@link Space Spaces} to be checked for
     * @return {@code true} if this handle manager contains all the specified {@link Space Spaces}
     */
    @Override
    public boolean containsSpaces(Collection<Space> spaces) {
        return spaces.stream().allMatch(this.spaces::containsValue);
    }

    /**
     * @param handles identifying {@link Handle Handles} of the {@link Space Spaces} to be checked for
     * @return {@code true} if this handle manager contains {@link Space Spaces} with all the specified
     * {@link Handle Handles}
     */
    @Override
    public boolean containsSpacesByHandle(Collection<Handle> handles) {
        return spaces.containsAll(handles);
    }

    /**
     * @param ids ids of the {@link Space Spaces} to be checked for
     * @return {@code true} if this handle manager contains {@link Space Spaces} with all the specified ids
     */
    @Override
    public boolean containsSpacesById(Collection<String> ids) {
        return spaces.containsAllById(ids);
    }

    /**
     * @param space {@link Space} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeSpace(Space space) {
        checkHardcoded(space);

        spaceSpace.removeHandle(space.getHandle());

        return spaces.remove(space.getHandle(), space);
    }

    /**
     * @param handle identifying {@link Handle} of the {@link Space} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeSpaceByHandle(Handle handle) {
        checkHardcoded(handle);

        spaceSpace.removeHandle(handle);

        return spaces.remove(handle) != null;
    }

    /**
     * @param id id of the {@link Space} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeSpaceById(String id) {
        checkHardcoded(id);

        spaceSpace.removeHandleById(id);

        return spaces.removeById(id) != null;
    }

    /**
     * @param spaces {@link Space Spaces} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeSpaces(Collection<Space> spaces) {
        spaces.forEach(this::checkHardcoded);

        return removeSpacesByHandle(spaces
                .stream()
                .map(Space::getHandle)
                .collect(Collectors.toSet()));
    }

    /**
     * @param handles {@link Handle Handles} of the {@link Space Spaces} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeSpacesByHandle(Collection<Handle> handles) {
        handles.forEach(this::checkHardcoded);

        spaceSpace.removeHandles(handles);

        return spaces.removeAll(handles);
    }

    /**
     * @param ids ids of the {@link Space Spaces} to be removed
     * @return {@code true} if the {@link Space Spaces} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeSpacesById(Collection<String> ids) {
        ids.forEach(this::checkHardcoded);

        spaceSpace.removeHandlesById(ids);

        return spaces.removeAllById(ids);
    }

    /**
     * @return {@link Stream} of the {@link Space Spaces} present in this handle manager
     */
    @Override
    public Stream<Space> spaceStream() {
        return spaces.stream();
    }

    /**
     * @param id id of the requested tag {@link Handle}
     * @return tag {@link Handle} matching the specified id, or {@code null} if none are present
     */
    @Override
    public Handle getTag(String id) {
        return tagSpace.getHandle(id);
    }

    /**
     * @return all tag {@link Handle Handles} present in this handle manager
     */
    @Override
    public Collection<Handle> getTags() {
        return tagSpace.getHandles();
    }

    /**
     * @param id id of the tag {@link Handle} to be created
     * @return created {@link Handle}
     *
     * @throws IllegalArgumentException if a tag Handle with the specified id is already present
     */
    @Override
    public Handle createTag(String id) {
        return tagSpace.createHandle(id);
    }

    /**
     * Checks if a tag {@link Handle} is present with the specified id. If one is present, that tag Handle is returned.
     * If none are present, a new tag Handle with the specified id is created and returned.
     *
     * @param id id of the requested tag {@link Handle}
     * @return requested tag {@link Handle}
     */
    @Override
    public Handle getOrCreateTag(String id) {
        return tagSpace.getOrCreateHandle(id);
    }

    /**
     * @param tag tag {@link Handle} to be checked for
     * @return {@code true} if this handle manager contains the specified tag {@link Handle}
     */
    @Override
    public boolean containsTag(Handle tag) {
        return tagSpace.containsHandle(tag);
    }

    /**
     * @param id id of the tag {@link Handle} to be checked for
     * @return {@code true} if this handle manager contains a tag {@link Handle} with the specified id
     */
    @Override
    public boolean containsTagById(String id) {
        return tagSpace.containsHandleById(id);
    }

    /**
     * @param tags tag {@link Handle Handles} to be checked for
     * @return {@code true} if this handle manager contains all the specified tag {@link Handle Handles}
     */
    @Override
    public boolean containsTags(Collection<Handle> tags) {
        return tagSpace.containsHandles(tags);
    }

    /**
     * @param ids tag {@link Handle} ids to be checked for
     * @return {@code true} if this handle manager contains tag {@link Handle Handles} with all the specified ids
     */
    @Override
    public boolean containsTagsById(Collection<String> ids) {
        return tagSpace.containsHandlesById(ids);
    }

    /**
     * @param tag tag {@link Handle} to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeTag(Handle tag) {
        return tagSpace.removeHandle(tag);
    }

    /**
     * @param id id of the tag {@link Handle} to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeTagById(String id) {
        return tagSpace.removeHandleById(id);
    }

    /**
     * @param tags tag {@link Handle Handles} to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeTags(Collection<Handle> tags) {
        return tagSpace.removeHandles(tags);
    }

    /**
     * @param ids tag {@link Handle} ids to be removed
     * @return {@code true} if the tag {@link Handle Handles} of this handle manager changed as a result of this action
     */
    @Override
    public boolean removeTagsById(Collection<String> ids) {
        return tagSpace.removeHandlesById(ids);
    }

    /**
     * @return {@link Stream} of the tag {@link Handle Handles} present in this handle manager
     */
    @Override
    public Stream<Handle> tagStream() {
        return tagSpace.handleStream();
    }

    /**
     * Clears everything, retaining the two hardcoded {@link Space Spaces} and their identifying {@link Handle Handles}.
     */
    @Override
    public void clear() {
        tagSpace.getHandle().clearTags();
        spaceSpace.getHandle().clearTags();
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
