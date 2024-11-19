package me.datafox.dfxengine.handles;

import lombok.Getter;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.handles.utils.HandleUtils;
import me.datafox.dfxengine.handles.utils.UnmodifiableHandleMap;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static me.datafox.dfxengine.handles.utils.HandleStrings.notHandled;
import static me.datafox.dfxengine.handles.utils.HandleUtils.checkNullAndType;
import static me.datafox.dfxengine.handles.utils.HandleUtils.checkTag;

/**
 * An ordered implementation of {@link HandleMap} backed with a {@link TreeMap}.
 *
 * @param <T> the type of mapped values
 *
 * @author datafox
 */
public class TreeHandleMap<T> extends TreeMap<Handle,T> implements HandleMap<T> {
    private final Logger logger;

    /**
     * {@link Space} associated with this map.
     */
    @Getter
    private final Space space;
    private final Map<String,Handle> ids;

    /**
     * Public constructor for {@link TreeHandleMap}.
     *
     * @param space {@link Space} to be associated with this map
     * @param logger {@link Logger} for this map
     */
    public TreeHandleMap(Space space, Logger logger) {
        super();
        this.logger = logger;
        this.space = space;
        ids = new HashMap<>();
    }

    /**
     * Public constructor for {@link TreeHandleMap}. Uses {@link LoggerFactory#getLogger(Class)} with
     * {@link TreeHandleMap TreeHandleMap.class}.
     *
     * @param space {@link Space} to be associated with this map
     */
    public TreeHandleMap(Space space) {
        this(space, LoggerFactory.getLogger(TreeHandleMap.class));
    }

    /**
     * Returns {@code true} if this map contains a mapping for all the specified keys. The keys may either be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param keys {@link Handle} keys or their {@link String} ids whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for all the specified keys
     * @throws ClassCastException if any of the keys are of an inappropriate type for this map
     * @throws NullPointerException if any of the keys is {@code null}
     */
    @Override
    public boolean containsKeys(Collection<?> keys) {
        keys.forEach(key -> HandleUtils.checkNullAndType(key, logger));
        return keys.stream()
                .map(this::containsKeyInternal)
                .reduce(true, Boolean::logicalAnd);
    }

    /**
     * Associates a {@link Handled} value with its associated {@link Handle}. If the map previously contained a mapping
     * for the key, the old value is replaced by the specified value. The {@link Handle} key must be present in this
     * map's associated {@link Space}.
     *
     * @param value {@link Handled} value to be associated with its associated {@link Handle} key
     * @return the previous value associated with the key, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws ClassCastException if the value does not implement {@link Handled}
     * @throws NullPointerException if the specified value is {@code null}
     * @throws IllegalArgumentException if the value's associated {@link Handle} is not present in this map's associated
     * {@link Space}
     */
    @Override
    public T putHandled(T value) {
        if(!(value instanceof Handled)) {
            throw LogUtils.logExceptionAndGet(logger,
                    notHandled(value),
                    ClassCastException::new);
        }
        return put(((Handled) value).getHandle(), value);
    }

    /**
     * Returns an unmodifiable version of this map. All changes made to the original map will be reflected in the
     * returned one.
     *
     * @return unmodifiable version of this map
     */
    @Override
    public HandleMap<T> unmodifiable() {
        return new UnmodifiableHandleMap<>(this, logger);
    }

    /**
     * Returns all values mapped to keys containing the specified tag. The tag may be a {@link Handle} or its
     * {@link String} id.
     *
     * @param tag tag {@link Handle} or its {@link String} id
     * @return all values mapped to keys containing the specified tag
     * @throws ClassCastException if the tag is not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the tag is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} is not a tag
     */
    @Override
    public Collection<T> getByTag(Object tag) {
        checkNullAndType(tag, logger);
        checkTag(tag, logger);
        return keySet()
                .stream()
                .filter(h -> h.getTags().contains(tag))
                .map(this::get)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Returns all values mapped to keys containing the specified tags. The tags may be {@link Handle Handles} or their
     * {@link String} ids.
     *
     * @param tags tag {@link Handle Handles} or their {@link String} ids
     * @return all values mapped to keys containing the specified tags
     * @throws ClassCastException if any of the tags are not {@link Handle Handles} or a {@link String Strings}
     * @throws NullPointerException if any of the tags is {@code null}
     * @throws IllegalArgumentException if any of the {@link Handle Handles} is not a tag
     */
    @Override
    public Collection<T> getByTags(Collection<?> tags) {
        tags.forEach(handle -> checkNullAndType(handle, logger));
        tags.forEach(handle -> checkTag(handle, logger));
        return keySet()
                .stream()
                .filter(h -> h.getTags().containsAll(tags))
                .map(this::get)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key. The key may either be a {@link Handle}
     * or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     * @throws ClassCastException if the key is of an inappropriate type for this map
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public boolean containsKey(Object key) {
        HandleUtils.checkNullAndType(key, logger);
        return containsKeyInternal(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if none is present. The key may either be
     * a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if none is present
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public T get(Object key) {
        HandleUtils.checkNullAndType(key, logger);
        return getInternal(key);
    }

    /**
     * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value. The {@link Handle} key must be present in this map's
     * associated {@link Space}.
     *
     * @param key {@link Handle} key with which the specified value is to be associated with
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws NullPointerException if the specified key or value is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} key is not present in this map's associated {@link Space}
     */
    @Override
    public T put(Handle key, T value) {
        HandleUtils.checkNullAndSpace(key, space, logger);
        HandleUtils.checkNullValue(value, logger);
        return putInternal(key, value);
    }

    /**
     * Removes the mapping for a key from this map if it is present. Returns the value to which this map previously
     * associated the key, or {@code null} if the map contained no mapping for the key. The key may either be a
     * {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose mapping is to be removed from this map
     * @return the previous value associated with {@code key}, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this map
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public T remove(Object key) {
        HandleUtils.checkNullAndType(key, logger);
        return removeInternal(key);
    }

    /**
     * Copies all of the mappings from the specified map to this map. The effect of this call is equivalent to that
     * of calling {@link #put(Handle, Object)} on this map once for each mapping from key to value in the specified map.
     * All {@link Handle} keys must be present in this map's associated {@link Space}.
     *
     * @param map mappings to be stored in this map
     * @throws UnsupportedOperationException if the {@code putAll} operation is not supported by this map
     * @throws NullPointerException if the specified map is {@code null} or contains {@code null} keys or values
     * @throws IllegalArgumentException if the specified map contains a {@link Handle} key that is not present in this
     * map's associated {@link Space}
     */
    @Override
    public void putAll(Map<? extends Handle,? extends T> map) {
        map.keySet().forEach(handle -> HandleUtils.checkNullAndSpace(handle, space, logger));
        map.values().forEach(value -> HandleUtils.checkNullValue(value, logger));
        map.forEach(this::putInternal);
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key, or the specified default value if none
     * is present. The key may either be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or the specified default value if this map contains no
     * mapping for the key
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    public T getOrDefault(Object key, T defaultValue) {
        HandleUtils.checkNullAndType(key, logger);
        HandleUtils.checkNullValue(defaultValue, logger);
        return Objects.requireNonNullElse(getInternal(key), defaultValue);
    }

    /**
     * Associates the specified key with the specified value and returns {@code null} if the specified key is not
     * already associated with a value, otherwise returns the current value.
     *
     * @param key {@link Handle} key with which the specified value is to be associated with
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or {@code null} if there was no mapping for the key
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws NullPointerException if the specified key or value is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} key is not present in this map's associated {@link Space}
     */
    @Override
    public T putIfAbsent(Handle key, T value) {
        HandleUtils.checkNullAndSpace(key, space, logger);
        HandleUtils.checkNullValue(value, logger);
        if(containsKeyInternal(key)) {
            return super.get(key);
        }
        return putInternal(key, value);
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to the specified value. The key may either
     * be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this map
     * @throws ClassCastException if the key is of not a {@link Handle} or a {@link String}
     * @throws NullPointerException if the specified key or value is {@code null}
     */
    @Override
    public boolean remove(Object key, Object value) {
        HandleUtils.checkNullAndType(key, logger);
        HandleUtils.checkNullValue(value, logger);
        T obj = get(key);
        if(obj == value) {
            removeInternal(key);
            return true;
        }
        return false;
    }

    private boolean containsKeyInternal(Object key) {
        if(key instanceof String) {
            return ids.containsKey(key);
        }
        return super.containsKey(key);
    }

    private T getInternal(Object key) {
        Handle handle;
        if(key instanceof String) {
            handle = ids.get(key);
        } else {
            handle = (Handle) key;
        }
        if(handle == null) {
            return null;
        }
        return super.get(handle);
    }

    private T putInternal(Handle key, T value) {
        ids.put(key.getId(), key);
        return super.put(key, value);
    }

    private T removeInternal(Object key) {
        Handle handle = null;
        if(key instanceof String) {
            if(ids.containsKey(key)) {
                handle = ids.get(key);
            }
        } else {
            handle = (Handle) key;
        }
        if(handle == null) {
            return null;
        }
        ids.remove(handle.getId());
        return super.remove(handle);
    }
}
