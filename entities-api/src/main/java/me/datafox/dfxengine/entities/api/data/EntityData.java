package me.datafox.dfxengine.entities.api.data;

import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityData<T> extends Handled {
    DataType<T> getType();

    T getData();

    boolean isStateful();

    NodeData<T> toNodeData();
}
