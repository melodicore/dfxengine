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

    public boolean equals(final Object o) {
        if(o == this) return true;
        if(!(o instanceof HandleImpl)) return false;
        final HandleImpl other = (HandleImpl) o;
        final Object this$handleManager = this.getHandleManager();
        final Object other$handleManager = other.getHandleManager();
        if(this$handleManager == null ? other$handleManager != null : !this$handleManager.equals(other$handleManager))
            return false;
        final Object this$space = this.getSpace();
        final Object other$space = other.getSpace();
        if(this$space == null ? other$space != null : !this$space.equals(other$space)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if(this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        if(this.getIndex() != other.getIndex()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $handleManager = this.getHandleManager();
        result = result * PRIME + ($handleManager == null ? 43 : $handleManager.hashCode());
        final Object $space = this.getSpace();
        result = result * PRIME + ($space == null ? 43 : $space.hashCode());
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final long $index = this.getIndex();
        result = result * PRIME + (int) ($index >>> 32 ^ $index);
        return result;
    }
}
