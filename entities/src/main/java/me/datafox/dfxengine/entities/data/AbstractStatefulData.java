package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.StatefulEntityData;
import me.datafox.dfxengine.entities.api.state.DataState;
import me.datafox.dfxengine.handles.api.Handle;

/**
 * @author datafox
 */
public abstract class AbstractStatefulData<T> extends AbstractData<T> implements StatefulEntityData<T> {
    protected AbstractStatefulData(Handle handle, Handle typeHandle, T data) {
        super(handle, typeHandle, data);
    }

    protected static <T extends DataState> T castState(DataState state, Class<T> type) {
        if(!type.isInstance(state)) {
            throw new IllegalArgumentException("invalid state");
        }
        return type.cast(state);
    }
}
