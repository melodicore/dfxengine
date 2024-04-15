package me.datafox.dfxengine.handles.api;

import java.util.Collection;
import java.util.Map;

/**
 * An extension of {@link Map} that may only contain {@link Handle} keys from a specified {@link Space}. Also has
 * extra functionality to query entries based on their {@link String} id.
 *
 * @author datafox
 */
public interface HandleMap<T> extends Map<Handle, T> {
    /**
     * Returns the {@link Space} associated with this map. All {@link Handle} keys present in this map must be
     * associated with this space.
     *
     * @return {@link Space} associated with this map
     */
    Space getSpace();

    /**
     * Returns {@code true} if this map contains a mapping for all the specified keys. The keys may either be
     * {@link Handle Handles} or their {@link String} ids.
     *
     * @param keys {@link Handle} keys or their {@link String} ids whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for all the specified keys
     * @throws ClassCastException if any of the keys are of an inappropriate type for this map
     * @throws NullPointerException if any of the keys is {@code null}
     */
    boolean containsKeys(Collection<?> keys);

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
    T putHandled(T value);

    /**
     * Returns an unmodifiable version of this map. All changes made to the original map will be reflected in the
     * returned one.
     *
     * @return unmodifiable version of this map
     */
    HandleMap<T> unmodifiable();

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
    Collection<T> getByTag(Object tag);

    /**
     * Returns all values mapped to keys containing the specified tag. The tags may be {@link Handle Handles} or their
     * {@link String} ids.
     *
     * @param tags tag {@link Handle Handles} or their {@link String} ids
     * @return all values mapped to keys containing the specified tags
     * @throws ClassCastException if any of the tags are not {@link Handle Handles} or a {@link String Strings}
     * @throws NullPointerException if any of the tags is {@code null}
     * @throws IllegalArgumentException if any of the {@link Handle Handles} is not a tag
     */
    Collection<T> getByTags(Collection<?> tags);

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
    boolean containsKey(Object key);

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
    T get(Object key);

    /**
     * Associates the specified value with the specified key in this map. If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value. The {@link Handle} key must be present in this map's
     * associated {@link Space}.
     *
     * @param key {@link Handle} key with which the specified value is to be associated with
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws ClassCastException if the key or value is of an inappropriate type for this map
     * @throws NullPointerException if the specified key or value is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} key is not present in this map's associated {@link Space}
     */
    @Override
    T put(Handle key, T value);

    /**
     * Removes the mapping for a key from this map if it is present. Returns the value to which this map previously
     * associated the key, or {@code null} if the map contained no mapping for the key. The key may either be a
     * {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose mapping is to be removed from this map
     * @return the previous value associated with {@code key}, or {@code null} if none was present
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this map
     * @throws ClassCastException if the key is of an inappropriate type for this map
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    T remove(Object key);

    /**
     * Copies all of the mappings from the specified map to this map. The effect of this call is equivalent to that
     * of calling {@link #put(Handle, Object)} on this map once for each mapping from key to value in the specified map.
     * All {@link Handle} keys must be present in this map's associated {@link Space}.
     *
     * @param map mappings to be stored in this map
     * @throws UnsupportedOperationException if the {@code putAll} operation is not supported by this map
     * @throws ClassCastException if a key or value is of an inappropriate type for this map
     * @throws NullPointerException if the specified map is {@code null} or contains {@code null} keys or values
     * @throws IllegalArgumentException if the specified map contains a {@link Handle} key that is not present in this
     * map's associated {@link Space}
     */
    @Override
    void putAll(Map<? extends Handle,? extends T> map);

    /**
     * Returns {@code true} if this map contains a mapping for the specified key, or the specified default value if none
     * is present. The key may either be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or the specified default value if this map contains no
     * mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for this map
     * @throws NullPointerException if the specified key is {@code null}
     */
    @Override
    T getOrDefault(Object key, T defaultValue);

    /**
     * Associates the specified key with the specified value and returns {@code null} if the specified key is not
     * already associated with a value, otherwise returns the current value.
     *
     * @param key {@link Handle} key with which the specified value is to be associated with
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or {@code null} if there was no mapping for the key
     * @throws UnsupportedOperationException if the {@code put} operation is not supported by this map
     * @throws ClassCastException if the key or value is of an inappropriate type for this map
     * @throws NullPointerException if the specified key or value is {@code null}
     * @throws IllegalArgumentException if the {@link Handle} key is not present in this map's associated {@link Space}
     */
    @Override
    T putIfAbsent(Handle key, T value);

    /**
     * Removes the entry for the specified key only if it is currently mapped to the specified value. The key may either
     * be a {@link Handle} or its {@link String} id.
     *
     * @param key {@link Handle} key or its {@link String} id with which the specified value is associated
     * @param value value expected to be associated with the specified key
     * @return {@code true} if the value was removed
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this map
     * @throws ClassCastException if the key or value is of an inappropriate type for this map
     * @throws NullPointerException if the specified key or value is {@code null}
     */
    @Override
    boolean remove(Object key, Object value);
}
