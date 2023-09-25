package me.datafox.dfxengine.handles;

import lombok.*;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleManager;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.collection.HashHandleSet;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An identifier class with a tagging system. A handle in its most bare-bones understanding contains a {@link String}
 * id, a {@link Space} which is a namespace for ids, and zero or more tags, which are also handles. In practice a handle
 * also contains an index used for ordering, and a reference to a {@link HandleManager} which is used to manipulate
 * handles and Spaces, including tags. Handles are primarily designed to be used as map keys and for other identifying
 * purposes.
 *
 * @author datafox
 */
@Data
public final class HandleImpl implements Handle {
    @EqualsAndHashCode.Exclude
    @NonNull
    private final HandleManager handleManager;

    @NonNull
    private final Space space;

    @EqualsAndHashCode.Exclude
    @NonNull
    private final String id;

    private final long index;

    @Getter(AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    private final HandleSet tags;

    @Builder
    private HandleImpl(@NonNull Space space, @NonNull String id, long index, @Singular Collection<Handle> tags) {
        super();
        handleManager = space.getHandleManager();
        this.space = space;
        this.id = id;
        this.index = index;
        this.tags = new HashHandleSet(((HandleManagerImpl) handleManager).getTagSpace(), tags);
    }

    HandleImpl(@NonNull Space space, @NonNull String id, long index) {
        handleManager = space.getHandleManager();
        this.space = space;
        this.id = id;
        this.index = index;
        tags = null;
    }

    /**
     * @return {@link HandleManager} associated with this handle
     */
    @Override
    public HandleManager getHandleManager() {
        return handleManager;
    }

    /**
     * @return {@link Space} containing this handle
     */
    @Override
    public Space getSpace() {
        return space;
    }

    /**
     * @return {@link String} id of this handle
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * The index value used for ordering handles within a {@link Space}.
     *
     * @return index of this handle
     */
    @Override
    public long getIndex() {
        return index;
    }

    /**
     * @param id id to be checked for
     * @return {@code true} if the specified id matches the id of this handle
     */
    @Override
    public boolean isId(String id) {
        return this.id.equals(id);
    }

    /**
     * @return all tags associated with this handle
     */
    @Override
    public Collection<Handle> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * @param tag handle to be added as a tag
     * @return {@code true} if the tags of this handle changed as a result of this action
     *
     * @throws IllegalArgumentException when the specified handle is not a part of the tags {@link Space}. Details for
     * hardcoded Spaces are documented in {@link HandleManager}
     */
    @Override
    public boolean addTag(Handle tag) {
        return tags.add(tag);
    }

    /**
     * Adds a tag based on the specified id for the tag. If a tag with the specified id does not exist in the tags
     * {@link Space}, one is automatically created. Details for hardcoded Spaces are documented in
     * {@link HandleManager}.
     *
     * @param id id of the handle to be added as a tag
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    @Override
    public boolean addTagById(String id) {
        return addTag(tags.getSpace().getOrCreateHandle(id));
    }

    /**
     * @param tags handles to be added as a tags
     * @return {@code true} if the tags of this handle changed as a result of this action
     *
     * @throws IllegalArgumentException when any of the specified handles are not a part of the tags {@link Space}.
     * Details for hardcoded Spaces are documented in {@link HandleManager}
     */
    @Override
    public boolean addTags(Collection<Handle> tags) {
        return this.tags.addAll(tags);
    }

    /**
     * Adds tags based on the specified ids for the tags. If a tag with the specified id does not exist in the tags
     * {@link Space}, one is automatically created. Details for hardcoded Spaces are documented in
     * {@link HandleManager}.
     *
     * @param ids ids for the handles to be added as a tags
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    @Override
    public boolean addTagsById(Collection<String> ids) {
        return addTags(ids.stream()
                .map(tags.getSpace()::getOrCreateHandle)
                .collect(Collectors.toSet()));
    }

    /**
     * @param tag tag to be checked for
     * @return {@code true} if this handle contains the specified tag
     */
    @Override
    public boolean containsTag(Handle tag) {
        return tags.contains(tag);
    }

    /**
     * @param id id of a tag to be checked for
     * @return {@code true} if this handle contains a tag with the specified id
     */
    @Override
    public boolean containsTagById(String id) {
        return tags.containsById(id);
    }

    /**
     * @param tags tags to be checked for
     * @return {@code true} if this handle contains all the specified tags
     */
    @Override
    public boolean containsTags(Collection<Handle> tags) {
        return this.tags.containsAll(tags);
    }

    /**
     * @param ids ids for the tags to be checked for
     * @return {@code true} if this handle contains tags with all the specified ids
     */
    @Override
    public boolean containsTagsById(Collection<String> ids) {
        return tags.containsAllById(ids);
    }

    /**
     * @param tag tag to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    @Override
    public boolean removeTag(Handle tag) {
        return tags.remove(tag);
    }

    /**
     * @param id id of a tag to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    @Override
    public boolean removeTagById(String id) {
        return tags.removeById(id);
    }

    /**
     * @param tags tags to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    @Override
    public boolean removeTags(Collection<Handle> tags) {
        return this.tags.removeAll(tags);
    }

    /**
     * @param ids ids of the tags to be removed
     * @return {@code true} if the tags of this handle changed as a result of this action
     */
    @Override
    public boolean removeTagsById(Collection<String> ids) {
        return tags.removeAllById(ids);
    }

    /**
     * @return {@link Stream} of the tags present in this handle
     */
    @Override
    public Stream<Handle> tagStream() {
        return tags.stream();
    }

    /**
     * Clears all tags from this handle.
     */
    @Override
    public void clearTags() {
        tags.clear();
    }

    /**
     * @return {@link String} representation of this handle in format <i>spaceId:handleId</i>
     */
    @Override
    public String toString() {
        return String.format("%s:%s", space.getHandle().getId(), getId());
    }

    void bootstrap(Space tagSpace) {
        try {
            Field f = HandleImpl.class.getDeclaredField("tags");
            f.trySetAccessible();
            f.set(this, new HashHandleSet(tagSpace));
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
