package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface Space extends Comparable<Space> {
    Handle getHandle();

    boolean isHandle(Handle handle);

    boolean isId(String id);

    Handle getHandle(String id);

    Collection<Handle> getHandles();

    Handle createHandle(String id);

    Handle getOrCreateHandle(String id);

    boolean containsHandle(Handle handle);

    boolean containsHandleById(String id);

    boolean removeHandle(Handle handle);

    boolean removeHandleById(String id);

    boolean removeHandles(Collection<Handle> handles);

    boolean removeHandlesById(Collection<String> ids);

    Stream<Handle> handleStream();

    @Override
    default int compareTo(Space o) {
        return getHandle().compareTo(o.getHandle());
    }
}
