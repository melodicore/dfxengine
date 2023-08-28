package me.datafox.dfxengine.handles.collection;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.handles.utils.HandleStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link HandleMap} backed with a {@link HashMap}.
 *
 * @author datafox
 */
public class HashHandleMap<T> extends HashMap<Handle,T> implements HandleMap<T> {
    private final Logger logger;

    @Getter
    private final Space space;

    private final Map<String,Handle> idMap;

    public HashHandleMap(Space space) {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
        this.space = space;
        idMap = new HashMap<>();
    }

    public HashHandleMap(Space space, Map<Handle,T> handles) {
        this(space);

        putAll(handles);
    }

    @Override
    public T put(Handle handle, T value) {
        checkSpace(handle);

        return putInternal(handle, value);
    }

    @Override
    public void putAll(Map<? extends Handle,? extends T> map) {
        map.keySet().forEach(this::checkSpace);

        super.putAll(map);
    }

    @Override
    public T remove(Object key) {
        T previous = super.remove(key);

        if(key instanceof Handle) {
            idMap.remove(((Handle) key).getId(), key);
        }

        return previous;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean removed = super.remove(key, value);

        if(removed && key instanceof Handle) {
            idMap.remove(((Handle) key).getId(), key);
        }

        return removed;
    }

    @Override
    public void clear() {
        idMap.clear();
        super.clear();
    }

    @Override
    public boolean containsById(String id) {
        return idMap.containsKey(id);
    }

    @Override
    public boolean containsAll(Collection<Handle> handles) {
        return keySet().containsAll(handles);
    }

    @Override
    public boolean containsAllById(Collection<String> ids) {
        return idMap.keySet().containsAll(ids);
    }

    @Override
    public T getById(String id) {
        Handle handle = idMap.get(id);

        if(handle == null) {
            return null;
        }

        return get(handle);
    }

    @Override
    public T removeById(String id) {
        Handle handle = idMap.remove(id);

        if(handle == null) {
            return null;
        }

        return super.remove(handle);
    }

    @Override
    public boolean removeAll(Collection<Handle> handles) {
        boolean changed = false;

        for(Handle handle : handles) {
            if(remove(handle) != null) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public boolean removeAllById(Collection<String> ids) {
        boolean changed = false;

        for(String id : ids) {
            if(removeById(id) != null) {
                changed = true;
            }
        }
        return changed;
    }

    private void checkSpace(Handle handle) {
        if(!space.equals(handle.getSpace())) {
            throw LogUtils.logExceptionAndGet(logger,
                    HandleStrings.spaceMismatchHandleMap(handle, space),
                    IllegalArgumentException::new);
        }
    }

    private T putInternal(Handle handle, T value) {
        T previous = super.put(handle, value);

        idMap.put(handle.getId(), handle);

        return previous;
    }
}
