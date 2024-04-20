package me.datafox.dfxengine.handles.utils;

import me.datafox.dfxengine.utils.LogUtils;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.function.Consumer;

import static me.datafox.dfxengine.handles.utils.HandleStrings.UNMODIFIABLE_SET;

/**
 * Unmodifiable {@link Iterator} that mirrors another iterator. Used internally by
 * {@link UnmodifiableHandleSet#iterator()}.
 *
 * @author datafox
 */
public class UnmodifiableIterator<T> implements Iterator<T> {
    private final Logger logger;
    private final Iterator<T> it;

    public UnmodifiableIterator(Iterator<T> it, Logger logger) {
        this.it = it;
        this.logger = logger;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        return it.next();
    }

    @Override
    public void remove() {
        throw LogUtils.logExceptionAndGet(logger,
                UNMODIFIABLE_SET,
                UnsupportedOperationException::new);
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        it.forEachRemaining(action);
    }

    @Override
    public int hashCode() {
        return it.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return it.equals(obj);
    }

    @Override
    public String toString() {
        return it.toString();
    }
}
