package me.datafox.dfxengine.entities.data;

import me.datafox.dfxengine.entities.api.StatefulEntityData;
import me.datafox.dfxengine.entities.api.state.DataState;

/**
 * @author datafox
 */
public abstract class AbstractStatefulData<T> extends AbstractData<T> implements StatefulEntityData<T> {
    protected AbstractStatefulData(Class<T> type, String handle) {
        super(type, handle);
    }

    protected static <T extends DataState> T castState(DataState state, Class<T> type) {
        if(!type.isInstance(state)) {
            throw new IllegalArgumentException("invalid state");
        }
        return type.cast(state);
    }
}
