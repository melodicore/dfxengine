package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleSet;
import me.datafox.dfxengine.handles.api.Space;
import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static me.datafox.dfxengine.handles.utils.HandleStrings.UNMODIFIABLE_SET;

/**
 * Unmodifiable {@link HandleSet} that mirrors another handle set. Used internally by implementations of
 * {@link HandleSet#unmodifiable()}.
 *
 * @author datafox
 */
@SuppressWarnings("MissingJavadoc")
public class UnmodifiableHandleSet implements HandleSet {
    private final Logger logger;
    private final HandleSet set;

    public UnmodifiableHandleSet(HandleSet set, Logger logger) {
        this.set = set;
        this.logger = logger;
    }

    @Override
    public Space getSpace() {
        return set.getSpace();
    }

    @Override
    public Handle get(String id) {
        return set.get(id);
    }

    @Override
    public Handle add(String id) {
        throw unmodifiableException();
    }

    @Override
    public HandleSet unmodifiable() {
        return this;
    }

    @Override
    public Collection<Handle> getByTag(Object tag) {
        return set.getByTag(tag);
    }

    @Override
    public Collection<Handle> getByTags(Collection<?> tags) {
        return set.getByTags(tags);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<Handle> iterator() {
        return new UnmodifiableIterator<>(set.iterator(), logger);
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(Handle handle) {
        throw unmodifiableException();
    }

    @Override
    public boolean remove(Object o) {
        throw unmodifiableException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Handle> c) {
        throw unmodifiableException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw unmodifiableException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw unmodifiableException();
    }

    @Override
    public void clear() {
        throw unmodifiableException();
    }

    @Override
    public Spliterator<Handle> spliterator() {
        return Spliterators.spliterator(set, Spliterator.IMMUTABLE);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return set.toArray(generator);
    }

    @Override
    public boolean removeIf(Predicate<? super Handle> filter) {
        return set.removeIf(filter);
    }

    @Override
    public Stream<Handle> stream() {
        return set.stream();
    }

    @Override
    public Stream<Handle> parallelStream() {
        return set.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super Handle> action) {
        set.forEach(action);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return set.equals(obj);
    }

    @Override
    public String toString() {
        return set.toString();
    }
    
    private UnsupportedOperationException unmodifiableException() {
        return LogUtils.logExceptionAndGet(logger,
                UNMODIFIABLE_SET,
                UnsupportedOperationException::new);
    }
}
