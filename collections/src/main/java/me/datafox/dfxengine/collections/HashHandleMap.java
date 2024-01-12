package me.datafox.dfxengine.collections;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.api.collection.HandleMap;
import me.datafox.dfxengine.collections.utils.CollectionStrings;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An extension of {@link HashMap} that can only use {@link Handle Handles} of the {@link Space} associated with this
 * map as its keys. Implements {@link HandleMap} for various helper methods related to Handles.
 *
 * @author datafox
 */
public class HashHandleMap<T> extends HashMap<Handle,T> implements HandleMap<T> {
    private final Logger logger;

    private final Space space;

    private final Map<String,Handle> idMap;

    /**
     * @param space {@link Space} to be associated with this map
     */
    public HashHandleMap(Space space) {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
        this.space = space;
        idMap = new HashMap<>();
    }

    /**
     * @param space {@link Space} to be associated with this map
     * @param handles mappings to be stored in this map
     */
    public HashHandleMap(Space space, Map<Handle,T> handles) {
        this(space);

        putAll(handles);
    }

    /**
     * Associates the specified value with the specified {@link Handle} in this map. If the map previously contained a
     * mapping for the Handle, the old value is replaced. The Handle used as the key must be present in the
     * {@link Space} associated with this map.
     *
     * @param handle {@link Handle} with which the specified value is to be associated
     * @param value value to be associated with the specified {@link Handle}
     * @return the previous value associated with the specified {@link Handle}, or {@code null} if there was no mapping
     * for the Handle
     *
     * @throws IllegalArgumentException if the specified {@link Handle} is not present in the {@link Space}
     * associated with this map
     */
    @Override
    public T put(Handle handle, T value) {
        checkSpace(handle);

        return putInternal(handle, value);
    }

    /**
     * Copies all of the mappings from the specified map to this map. These mappings will replace any mappings that this
     * map had for any of the {@link Handle Handles} currently in the specified map. All Handles used as the keys must
     * be present in the {@link Space} associated with this map.
     *
     * @param map mappings to be stored in this map
     *
     * @throws IllegalArgumentException if any of the specified {@link Handle Handles} are not present in the
     * {@link Space} associated with this map
     */
    @Override
    public void putAll(Map<? extends Handle,? extends T> map) {
        map.keySet().forEach(this::checkSpace);

        map.forEach(this::putInternal);
    }

    /**
     * Removes the mapping for the specified {@link Handle} from this map if present.
     *
     * @param  handle {@link Handle} whose mapping is to be removed from the map
     * @return the previous value associated with the specified {@link Handle}, or {@code null} if there was no mapping
     * for the specified key.
     */
    @Override
    public T remove(Object handle) {
        T previous = super.remove(handle);

        if(handle instanceof Handle) {
            idMap.remove(((Handle) handle).getId(), handle);
        }

        return previous;
    }

    /**
     * Removes the entry for the specified {@link Handle} only if it is currently mapped to the specified value.
     *
     * @param handle {@link Handle} with which the specified value is associated
     * @param value value expected to be associated with the specified {@link Handle}
     * @return {@code true} if the value was removed
     */
    @Override
    public boolean remove(Object handle, Object value) {
        boolean removed = super.remove(handle, value);

        if(removed && handle instanceof Handle) {
            idMap.remove(((Handle) handle).getId(), handle);
        }

        return removed;
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after this call returns.
     */
    @Override
    public void clear() {
        idMap.clear();
        super.clear();
    }

    /**
     * @return {@link Space} associated with this map
     */
    @Override
    public Space getSpace() {
        return space;
    }

    /**
     * Associates the specified value with its associated {@link Handle} in this map. If the map previously contained a
     * mapping for the key, the old value is replaced. Because Java does not support union types, the specified value
     * must implement {@link Handled}, and an exception is thrown otherwise.
     *
     * @param value value implementing {@link Handled} to be associated in this map with its associated {@link Handle}
     * as a key
     * @return the previously associated value in this map, or {@code null} if there was no previous association
     *
     * @throws IllegalArgumentException if the specified value does not implement {@link Handled}, or if the associated
     * {@link Handle} is not present in the {@link Space} associated with this map
     */
    @Override
    public T putHandled(T value) {
        if(!(value instanceof Handled)) {
            throw LogUtils.logExceptionAndGet(logger,
                    CollectionStrings.notHandledType(value),
                    IllegalArgumentException::new);
        }

        return put(((Handled) value).getHandle(), value);
    }

    /**
     * @param id id of the {@link Handle} to be checked for
     * @return {@code true} if this map contains a {@link Handle} with the specified id
     */
    @Override
    public boolean containsById(String id) {
        return idMap.containsKey(id);
    }

    /**
     * @param handles {@link Handle Handles} to be checked for
     * @return {@code true} if this map contains all the specified {@link Handle Handles}
     */
    @Override
    public boolean containsAll(Collection<Handle> handles) {
        return keySet().containsAll(handles);
    }

    /**
     * @param ids ids of the {@link Handle Handles} to be checked for
     * @return {@code true} if this map contains all {@link Handle Handles} with the specified ids
     */
    @Override
    public boolean containsAllById(Collection<String> ids) {
        return idMap.keySet().containsAll(ids);
    }

    /**
     * @param id id of the requested {@link Handle}
     * @return value matching the {@link Handle} with the specified id, or {@code null} if none are present
     */
    @Override
    public T getById(String id) {
        Handle handle = idMap.get(id);

        if(handle == null) {
            return null;
        }

        return get(handle);
    }

    /**
     * @param id id of the {@link Handle} to be removed
     * @return the value associated with the {@link Handle} with the specified id, or {@code null} if none were present
     */
    @Override
    public T removeById(String id) {
        Handle handle = idMap.remove(id);

        if(handle == null) {
            return null;
        }

        return super.remove(handle);
    }

    /**
     * @param handles {@link Handle Handles} to be removed
     * @return {@code true} if the contents of this map changed as a result of this action
     */
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

    /**
     * @param ids ids of the {@link Handle Handles} to be removed
     * @return {@code true} if the contents of this map changed as a result of this action
     */
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
                    CollectionStrings.spaceMismatchHandleMap(handle, space),
                    IllegalArgumentException::new);
        }
    }

    private T putInternal(Handle handle, T value) {
        T previous = super.put(handle, value);

        idMap.put(handle.getId(), handle);

        return previous;
    }
}
