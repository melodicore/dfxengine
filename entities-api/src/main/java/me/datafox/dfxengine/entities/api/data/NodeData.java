package me.datafox.dfxengine.entities.api.data;

import me.datafox.dfxengine.handles.api.Handle;

/**
* @author datafox
*/
public interface NodeData<T> {
    DataType<T> getType();

    T getData();

    EntityData<T> toEntityData(Handle handle);
}
