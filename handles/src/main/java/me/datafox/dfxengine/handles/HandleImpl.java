package me.datafox.dfxengine.handles;

import lombok.*;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleSet;
import me.datafox.dfxengine.handles.collection.TreeHandleSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author datafox
 */
@Data
public final class HandleImpl implements Handle {
    @NonNull
    private final Space space;

    @NonNull
    private final String id;

    @NonNull
    private final long index;

    @Getter(AccessLevel.NONE)
    private final HandleSet tags;

    @Builder
    private HandleImpl(@NonNull Space space, @NonNull String id, @NonNull long index, @Singular Collection<Handle> tags) {
        super();
        this.space = space;
        this.id = id;
        this.index = index;
        this.tags = new TreeHandleSet(HandleConstants.TAGS, tags);
    }

    @Override
    public boolean isId(String id) {
        return this.id.equals(id);
    }

    @Override
    public Set<Handle> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean addTag(Handle tag) {
        return tags.add(tag);
    }

    @Override
    public boolean addTagById(String id) {
        return addTag(HandleConstants.TAGS.getOrCreateHandle(id));
    }

    @Override
    public boolean addTags(Collection<Handle> tags) {
        return this.tags.addAll(tags);
    }

    @Override
    public boolean addTagsById(Collection<String> ids) {
        return addTags(ids.stream()
                .map(HandleConstants.TAGS::getOrCreateHandle)
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
        return getTags().stream();
    }

    @Override
    public String toString() {
        return String.format("%s:%s", space.getHandle().getId(), getId());
    }
}
