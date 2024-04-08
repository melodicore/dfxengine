package me.datafox.dfxengine.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * The object class map is a collection that was used internally by the Injector module before version 2.0.0. It is an
 * extension of the {@link FunctionClassMap} where the values are arbitrary objects and the function resolves the type
 * of the object.This ensures that all values in a list represented by a type are assignable to that type.
 * </p>
 * <p>
 * A new method, {@link #getAndCast(Class)}, is also defined, which casts all the objects in the list to the type the
 * list was retrieved with.
 * </p>
 * <p>
 * The collections module has been deprecated, and the collections are now in the respective modules that use them
 * </p>
 *
 * @author datafox
 */
@Deprecated
public class ObjectClassMap extends FunctionClassMap<Object> {
    /**
     * Constructor for ObjectClassMap
     */
    public ObjectClassMap() {
        super(Object::getClass);
    }

    /**
     * @param type type to be retrieved
     * @param <T> type to be retrieved
     * @return retrieved values cast to the type
     *
     * @see ObjectClassMap
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getAndCast(Class<T> type) {
        List<T> list = new ArrayList<>();

        if(delegate.containsKey(type)) {
            list.addAll(delegate
                    .get(type)
                    .stream()
                    .map(object -> (T) object)
                    .collect(Collectors.toList()));
        }

        return list;
    }
}
