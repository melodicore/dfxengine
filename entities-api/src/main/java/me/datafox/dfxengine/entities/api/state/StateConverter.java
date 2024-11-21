package me.datafox.dfxengine.entities.api.state;

import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;

/**
 * @author datafox
 */
public interface StateConverter<T, S extends DataState<T>> {
    DataType<T> getType();

    S createState(EntityData<T> data);
}
