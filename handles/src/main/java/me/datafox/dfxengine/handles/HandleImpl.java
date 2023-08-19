package me.datafox.dfxengine.handles;

import lombok.Data;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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

    private final Set<String> tags = new HashSet<>();

    public boolean isId(String id) {
        return this.id.equals(id);
    }

    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    public boolean addTags(Collection<String> tags) {
        return this.tags.addAll(tags);
    }

    public boolean containsTag(String tag) {
        return tags.contains(tag);
    }

    public boolean containsTags(Collection<String> tags) {
        return this.tags.containsAll(tags);
    }

    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    public boolean removeTags(Collection<String> tags) {
        return this.tags.removeAll(tags);
    }

    public Stream<String> tagStream() {
        return tags.stream();
    }
}
