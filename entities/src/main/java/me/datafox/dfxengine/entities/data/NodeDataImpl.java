package me.datafox.dfxengine.entities.data;

import lombok.Data;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.NodeData;

/**
 * @author datafox
 */
@Data
public class NodeDataImpl<T> implements NodeData<T> {
    private final DataType<T> type;

    private final T data;

    public NodeDataImpl(DataType<T> type, T data) {
        this.type = type;
        this.data = data;
    }
}
