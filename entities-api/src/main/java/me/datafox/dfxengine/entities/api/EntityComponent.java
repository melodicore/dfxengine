package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityComponent extends Handled {
    <T> HandleMap<EntityData<T>> getData(Class<T> type);

    HandleMap<EntityLink> getLinks();

    HandleMap<EntityAction> getActions();

    void link();

    void clear();

    void setState(ComponentState state);

    ComponentState getState();
}
