package me.datafox.dfxengine.collections;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.datafox.dfxengine.utils.ClassUtils;

import java.util.*;
import java.util.function.Function;

/**
 * <p>
 * The function class map is a collection that was used internally by the Injector module before version 2.0.0. It
 * stores references to objects in relation to a class hierarchy, determined by a {@link Function}.
 * </p>
 * <p>
 * Whenever a value is added, a type is resolved by the function, and the value is added to a list of values, contained
 * within a map where the key is the resolved type. After this, all superclasses and superinterfaces of the class are
 * resolved recursively and the original value is also added to the lists of values represented by those types.
 * </p>
 * <p>
 * This data structure ensures that whenever {@link #get(Class)} is called, the retrieved list contains all values which
 * are represented by the type or a descendant of the type the method was called with.
 * </p>
 *
 * @author datafox
 */
@EqualsAndHashCode
public class FunctionClassMap<T> {
    /**
     * Delegate map
     */
    protected final Map<Class<?>,List<T>> delegate;

    @ToString.Exclude
    private final Function<T,Class<?>> function;

    /**
     * @param function {@link Function} that resolves types from values
     */
    public FunctionClassMap(Function<T,Class<?>> function) {
        this.function = function;
        delegate = new HashMap<>();
    }

    /**
     * @param value value to be added
     */
    public void put(T value) {
        ClassUtils
                .getSuperclassesFor(function.apply(value))
                .forEach(type -> putInternal(type, value));
    }

    /**
     * @param values values to be added
     */
    public void putAll(Collection<? extends T> values) {
        for(T value : values) {
            put(value);
        }
    }

    /**
     * @param type type to be retrieved
     * @param <R> type to be checked for
     * @return list of values associated with the type
     *
     * @see FunctionClassMap
     */
    public <R> List<T> get(Class<R> type) {
        List<T> list = new ArrayList<>();

        if(delegate.containsKey(type)) {
            list.addAll(delegate.get(type));
        }

        return list;
    }

    /**
     * @param type type to be checked for
     * @param <R> type to be checked for
     * @return {@code true} if values are present for the type
     *
     * @see FunctionClassMap
     */
    public <R> boolean contains(Class<R> type) {
        return delegate.containsKey(type) && !delegate.get(type).isEmpty();
    }

    /**
     * @param type type to be checked for
     * @param <R> type to be checked for
     * @return {@code true} if a single value is present for the type
     */
    public <R> boolean isSingleton(Class<R> type) {
        List<T> list = delegate.get(type);

        if(list == null) return false;

        return list.size() == 1;
    }

    private <R> void putInternal(Class<R> type, T value) {
        if(!delegate.containsKey(type)) {
            delegate.put(type, new ArrayList<>());
        }

        delegate.get(type).add(value);
    }

    /**
     * @return {@link String} representation of this map
     */
    @Override
    public String toString() {
        return delegate.toString();
    }
}
