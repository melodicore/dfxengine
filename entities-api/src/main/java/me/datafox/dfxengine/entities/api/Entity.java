package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.state.EntityState;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface Entity extends Handled {
    HandleMap<EntityComponent> getComponents();

    boolean isSingleton();

    int getIndex();

    void setState(EntityState state);

    EntityState getState();
}
