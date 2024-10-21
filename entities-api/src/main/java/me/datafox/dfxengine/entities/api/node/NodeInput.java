package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.entities.api.data.DataType;

/**
* @author datafox
*/
public interface NodeInput<T> {
    Node getNode();

    DataType<T> getType();

    NodeOutput<T> getConnection();
}
