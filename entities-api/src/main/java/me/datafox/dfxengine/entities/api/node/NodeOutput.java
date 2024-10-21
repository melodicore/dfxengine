package me.datafox.dfxengine.entities.api.node;

import me.datafox.dfxengine.entities.api.data.DataType;

import java.util.List;

/**
* @author datafox
*/
public interface NodeOutput<T> {
    Node getNode();

    DataType<T> getType();

    List<NodeInput<T>> getConnections();
}
