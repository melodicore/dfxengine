package me.datafox.dfxengine.injector.collection;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.datafox.dfxengine.injector.Injector;
import me.datafox.dfxengine.injector.InjectorBuilder;
import me.datafox.dfxengine.utils.ClassUtils;

import java.util.*;
import java.util.function.Function;

/**
 * <p>
 * The FunctionClassMap is a collection used internally by the {@link InjectorBuilder} and {@link Injector}. It stores
 * references to objects in relation to a class hierarchy, determined by a function.
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
    protected final Map<Class<?>,List<T>> delegate;

    @ToString.Exclude
    private final Function<T,Class<?>> function;

    /**
     * @param function the function that resolves types from values
     */
    public FunctionClassMap(Function<T,Class<?>> function) {
        this.function = function;
        delegate = new HashMap<>();
    }

    /**
     * @param value the value to be added
     */
    public void put(T value) {
        ClassUtils
                .getSuperclassesFor(function.apply(value))
                .forEach(type -> putInternal(type, value));
    }

    /**
     * @param values the values to be added
     */
    public void putAll(Collection<T> values) {
        for(T value : values) {
            put(value);
        }
    }

    /**
     * @param type the type to be retrieved
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
     * @param type the type to be checked
     * @return true if values are present for the type, false otherwise
     *
     * @see FunctionClassMap
     */
    public <R> boolean contains(Class<R> type) {
        return delegate.containsKey(type) && !delegate.get(type).isEmpty();
    }

    /**
     * @param type the type to be checked
     * @return true if a single value is present for the type, false otherwise
     */
    public <R> boolean isSingleton(Class<R> type) {
        List<T> list = delegate.get(type);

        if(list == null) return false;

        return list.size() == 1;
    }

    /**
     * @param type the resolved type
     * @param value the value to be added
     */
    private <R> void putInternal(Class<R> type, T value) {
        if(!delegate.containsKey(type)) {
            delegate.put(type, new ArrayList<>());
        }

        delegate.get(type).add(value);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
