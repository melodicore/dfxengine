package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.state.ComponentState;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface EntityComponent extends Handled {
    HandleMap<HandleMap<EntityData<?>>> getData();

    HandleMap<EntityAction> getActions();

    void setState(ComponentState state);

    ComponentState getState();
}
