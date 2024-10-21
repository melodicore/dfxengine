package me.datafox.dfxengine.entities.api.data;

import me.datafox.dfxengine.entities.api.Context;

/**
 * @author datafox
 */
public interface DataReference<T> {
    T get(Context context);
}
