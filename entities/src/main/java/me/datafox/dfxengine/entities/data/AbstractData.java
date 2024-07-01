package me.datafox.dfxengine.entities.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.datafox.dfxengine.entities.api.EntityData;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
@Getter
public abstract class AbstractData<T> implements EntityData<T> {
    private final Handle handle;
    private final Handle typeHandle;
    @Setter(AccessLevel.PROTECTED)
    private T data;

    protected AbstractData(Handle handle, Handle typeHandle, T data) {
        this.handle = handle;
        this.typeHandle = typeHandle;
        this.data = data;
    }
}
