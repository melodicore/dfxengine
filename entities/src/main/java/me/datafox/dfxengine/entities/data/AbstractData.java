package me.datafox.dfxengine.entities.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.entities.api.EntityData;
import me.datafox.dfxengine.entities.utils.EntityHandles;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractData<T> implements EntityData<T> {
    private final Class<T> type;
    private final Handle handle;
    @Setter(AccessLevel.PROTECTED)
    private T data;

    protected AbstractData(Class<T> type, String handle) {
        this.type = type;
        this.handle = EntityHandles.getData().getOrCreateHandle(handle);
    }
}
