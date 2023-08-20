package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;

import java.util.Collection;
import java.util.SortedMap;
import java.util.stream.Stream;

/**
 * @author datafox
 */
public interface HandleMap<T> extends SortedMap<Handle,T> {
    boolean containsById(String id);

    boolean containsAll(Collection<Handle> handles);

    boolean containsAllById(Collection<String> ids);

    T getById(String id);

    T removeById(String id);

    boolean removeAll(Collection<Handle> handles);

    boolean removeAllById(Collection<String> ids);

    default Stream<T> stream() {
        return values().stream();
    }
}
