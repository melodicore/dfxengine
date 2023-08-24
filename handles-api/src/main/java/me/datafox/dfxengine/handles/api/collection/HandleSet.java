package me.datafox.dfxengine.handles.api.collection;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;

import java.util.Collection;
import java.util.SortedSet;

/**
 * @author datafox
 */
public interface HandleSet extends SortedSet<Handle> {
    Space getSpace();

    boolean containsById(String id);

    boolean containsAllById(Collection<String> ids);

    Handle get(String id);

    boolean removeById(String id);

    boolean removeAllById(Collection<String> ids);
}
