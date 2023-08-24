package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface HandleManager {
    Space getSpace(Handle handle);

    Space getSpaceById(String id);

    Handle getSpaceHandle(String id);

    Collection<Space> getSpaces();

    Collection<Handle> getSpaceHandles();

    Collection<Handle> getTags();

    Space createSpace(String id);

    Space getOrCreateSpace(String id);

    boolean containsSpace(Space space);

    boolean containsSpaceByHandle(Handle handle);

    boolean containsSpaceById(String id);

    boolean containsSpaces(Collection<Space> spaces);

    boolean containsSpacesByHandle(Collection<Handle> handles);

    boolean containsSpacesById(Collection<String> ids);

    boolean removeSpace(Space space);

    Space removeSpaceByHandle(Handle handle);

    Space removeSpaceById(String id);

    boolean removeSpaces(Collection<Space> spaces);

    boolean removeSpacesByHandle(Collection<Handle> handles);

    boolean removeSpacesById(Collection<String> ids);

    Stream<Space> spaceStream();

    Handle getTag(String id);

    Handle createTag(String id);

    Handle getOrCreateTag(String id);

    boolean containsTag(Handle tag);

    boolean containsTagById(String id);

    boolean containsTags(Collection<Handle> tags);

    boolean containsTagsById(Collection<String> ids);

    boolean removeTag(Handle tag);

    boolean removeTagById(String id);

    boolean removeTags(Collection<Handle> tags);

    boolean removeTagsById(Collection<String> ids);

    Stream<Handle> tagStream();

    void clear();
}
