package me.datafox.dfxengine.entities.data;

import lombok.Data;
import me.datafox.dfxengine.entities.api.data.DataType;
import me.datafox.dfxengine.entities.api.data.EntityData;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Data
public class EntityDataImpl<T> implements EntityData<T> {
    private final Handle handle;

    private final DataType<T> type;

    private final T data;
}
