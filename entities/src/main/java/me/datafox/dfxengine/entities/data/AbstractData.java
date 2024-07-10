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
    private final Handle handle;
    private final Handle typeHandle;
    @Setter(AccessLevel.PROTECTED)
    private T data;

    protected AbstractData(String handle, String typeHandle) {
        this.handle = EntityHandles.getData().getOrCreateHandle(handle);
        this.typeHandle = EntityHandles.getTypes().getOrCreateHandle(typeHandle);
    }
}
