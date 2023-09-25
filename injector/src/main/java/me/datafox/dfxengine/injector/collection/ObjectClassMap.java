package me.datafox.dfxengine.injector.collection;

import me.datafox.dfxengine.injector.Injector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * The object class map is a collection used internally by the {@link Injector}. It is an extension of the
 * {@link FunctionClassMap} where the values are arbitrary objects and the function resolves the type of the object.
 * This ensures that all values in a list represented by a type are assignable to that type.
 * </p>
 * <p>
 * A new method, {@link #getAndCast(Class)}, is also defined, which casts all the objects in the list to the type the
 * list was retrieved with.
 * </p>
 *
 * @author datafox
 */
public class ObjectClassMap extends FunctionClassMap<Object> {
    public ObjectClassMap() {
        super(Object::getClass);
    }

    /**
     * @param type the type to be retrieved
     * @return the retrieved values cast to the type
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
