package me.datafox.dfxengine.entities.data;

import lombok.Data;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.entities.api.data.NodeData;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Data
public class NodeDataImpl<T> implements NodeData<T> {
    private final DataType<T> type;

    private final T data;

    @Override
    public EntityData<T> toEntityData(Handle handle) {
        return new EntityDataImpl<>(handle, type, data);
    }
}
