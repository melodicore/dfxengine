package me.datafox.dfxengine.handles;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Handle {
    Space getSpace();

    String getId();

    Set<String> getTags();

    boolean isId(String id);

    boolean addTag(String tag);

    boolean addTags(Collection<String> tags);

    boolean containsTag(String tag);

    boolean containsTags(Collection<String> tags);

    boolean removeTag(String tag);

    boolean removeTags(Collection<String> tags);

    Stream<String> tagStream();
}
