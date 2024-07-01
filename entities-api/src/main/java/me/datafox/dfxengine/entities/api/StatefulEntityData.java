package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.state.DataState;

/**
 * @author datafox
 */
public interface StatefulEntityData<T> extends EntityData<T> {
    void setState(DataState state);

    DataState getState();
}
