package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface HandleManager {
    Space getSpace(Handle handle);

    Space getSpaceById(String id);

    Collection<Space> getSpaces();

    Space getSpaceSpace();

    Space getTagSpace();

    Space createSpace(String id);

    Space getOrCreateSpace(String id);

    boolean containsSpace(Space space);

    boolean containsSpaceByHandle(Handle handle);

    boolean containsSpaceById(String id);

    boolean removeSpace(Space space);

    Space removeSpaceByHandle(Handle handle);

    Space removeSpaceById(String id);

    boolean removeSpaces(Collection<Space> spaces);

    boolean removeSpacesByHandle(Collection<Handle> handles);

    boolean removeSpacesById(Collection<String> ids);

    Stream<Space> spaceStream();

    void clear();
}
