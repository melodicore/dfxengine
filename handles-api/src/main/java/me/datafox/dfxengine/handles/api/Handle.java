package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Handle extends Comparable<Handle> {
    HandleManager getHandleManager();

    Space getSpace();

    String getId();

    long getIndex();

    boolean isId(String id);

    Collection<Handle> getTags();

    boolean addTag(Handle tag);

    boolean addTagById(String id);

    boolean addTags(Collection<Handle> tags);

    boolean addTagsById(Collection<String> ids);

    boolean containsTag(Handle tag);

    boolean containsTagById(String id);

    boolean containsTags(Collection<Handle> tags);

    boolean containsTagsById(Collection<String> ids);

    boolean removeTag(Handle tag);

    boolean removeTagById(String id);

    boolean removeTags(Collection<Handle> tags);

    boolean removeTagsById(Collection<String> ids);

    Stream<Handle> tagStream();

    void clearTags();

    @Override
    default int compareTo(Handle o) {
        return Long.compare(getIndex(), o.getIndex());
    }
}
