package me.datafox.dfxengine.entities.api.data;

/**
* @author datafox
*/
public interface NodeData<T> {
    DataType<T> getType();

    T getData();
}
