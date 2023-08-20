package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Handle extends Comparable<Handle> {
    Space getSpace();

    String getId();

    long getIndex();

    boolean isId(String id);

    Set<Handle> getTags();

    boolean addTag(Handle tag);

    boolean addTags(Collection<Handle> tags);

    boolean containsTag(Handle tag);

    boolean containsTagById(String id);

    boolean containsTags(Collection<Handle> tags);

    boolean containsTagsById(Collection<String> ids);

    boolean removeTag(Handle tag);

    boolean removeTagById(String id);

    boolean removeTags(Collection<Handle> tags);

    boolean removeTagsById(Collection<String> ids);

    Stream<Handle> tagStream();

    @Override
    default int compareTo(Handle o) {
        return Long.compare(getIndex(), o.getIndex());
    }
}
