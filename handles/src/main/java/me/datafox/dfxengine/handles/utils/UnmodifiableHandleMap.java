package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static me.datafox.dfxengine.handles.utils.HandleStrings.UNMODIFIABLE_MAP;

/**
 * Unmodifiable {@link HandleMap} that mirrors another handle map. Used internally by implementations of
 * {@link HandleMap#unmodifiable()}.
 *
 * @param <T> the type of mapped values
 *
 * @author datafox
 */
public class UnmodifiableHandleMap<T> implements HandleMap<T> {
    private final Logger logger;
    private final HandleMap<T> map;

    public UnmodifiableHandleMap(HandleMap<T> map, Logger logger) {
        this.map = map;
        this.logger = logger;
    }

    @Override
    public Space getSpace() {
        return map.getSpace();
    }

    @Override
    public boolean containsKeys(Collection<?> keys) {
        return map.containsKeys(keys);
    }

    @Override
    public T putHandled(T value) {
        throw unmodifiableException();
    }

    @Override
    public HandleMap<T> unmodifiable() {
        return this;
    }

    @Override
    public Collection<T> getByTag(Object tag) {
        return map.getByTag(tag);
    }

    @Override
    public Collection<T> getByTags(Collection<?> tags) {
        return map.getByTags(tags);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return map.get(key);
    }

    @Override
    public T put(Handle key, T value) {
        throw unmodifiableException();
    }

    @Override
    public T remove(Object key) {
        throw unmodifiableException();
    }

    @Override
    public void putAll(Map<? extends Handle,? extends T> map) {
        throw unmodifiableException();
    }

    @Override
    public void clear() {
        throw unmodifiableException();
    }

    @Override
    public Set<Handle> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<T> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Handle,T>> entrySet() {
        return map.entrySet();
    }

    @Override
    public T getOrDefault(Object key, T defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public T putIfAbsent(Handle key, T value) {
        throw unmodifiableException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw unmodifiableException();
    }

    @Override
    public void forEach(BiConsumer<? super Handle,? super T> action) {
        map.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super Handle,? super T,? extends T> function) {
        throw unmodifiableException();
    }

    @Override
    public boolean replace(Handle key, T oldValue, T newValue) {
        throw unmodifiableException();
    }

    @Override
    public T replace(Handle key, T value) {
        throw unmodifiableException();
    }

    @Override
    public T computeIfAbsent(Handle key, Function<? super Handle,? extends T> mappingFunction) {
        throw unmodifiableException();
    }

    @Override
    public T computeIfPresent(Handle key, BiFunction<? super Handle,? super T,? extends T> remappingFunction) {
        throw unmodifiableException();
    }

    @Override
    public T compute(Handle key, BiFunction<? super Handle,? super T,? extends T> remappingFunction) {
        throw unmodifiableException();
    }

    @Override
    public T merge(Handle key, T value, BiFunction<? super T,? super T,? extends T> remappingFunction) {
        throw unmodifiableException();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return map.equals(obj);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    private UnsupportedOperationException unmodifiableException() {
        return LogUtils.logExceptionAndGet(logger,
                UNMODIFIABLE_MAP,
                UnsupportedOperationException::new);
    }
}
