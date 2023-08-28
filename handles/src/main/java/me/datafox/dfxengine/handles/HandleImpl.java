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
 * Implementation of {@link Handle}.
 *
 * @author datafox
 */
@Data
public final class HandleImpl implements Handle {
    @NonNull
    private final HandleManager handleManager;

    @NonNull
    private final Space space;

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

    @Override
    public boolean isId(String id) {
        return this.id.equals(id);
    }

    @Override
    public Collection<Handle> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean addTag(Handle tag) {
        return tags.add(tag);
    }

    @Override
    public boolean addTagById(String id) {
        return addTag(tags.getSpace().getOrCreateHandle(id));
    }

    @Override
    public boolean addTags(Collection<Handle> tags) {
        return this.tags.addAll(tags);
    }

    @Override
    public boolean addTagsById(Collection<String> ids) {
        return addTags(ids.stream()
                .map(tags.getSpace()::getOrCreateHandle)
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean containsTag(Handle tag) {
        return tags.contains(tag);
    }

    @Override
    public boolean containsTagById(String id) {
        return tags.containsById(id);
    }

    @Override
    public boolean containsTags(Collection<Handle> tags) {
        return this.tags.containsAll(tags);
    }

    @Override
    public boolean containsTagsById(Collection<String> ids) {
        return tags.containsAllById(ids);
    }

    @Override
    public boolean removeTag(Handle tag) {
        return tags.remove(tag);
    }

    @Override
    public boolean removeTagById(String id) {
        return tags.removeById(id);
    }

    @Override
    public boolean removeTags(Collection<Handle> tags) {
        return this.tags.removeAll(tags);
    }

    @Override
    public boolean removeTagsById(Collection<String> ids) {
        return tags.removeAllById(ids);
    }

    @Override
    public Stream<Handle> tagStream() {
        return tags.stream();
    }

    @Override
    public void clearTags() {
        tags.clear();
    }

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
