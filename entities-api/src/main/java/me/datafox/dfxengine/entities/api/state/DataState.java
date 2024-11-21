package me.datafox.dfxengine.entities.api.state;

import me.datafox.dfxengine.entities.api.data.DataType;

/**
 * @author datafox
 */
public interface DataState<T> {
    DataType<T> getType();

    String getHandle();

    void setState(T data);
}
