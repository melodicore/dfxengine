package me.datafox.dfxengine.entities.api.entity;

import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.handles.api.Handled;

/**
 * @author datafox
 */
public interface Entity extends Handled {
    HandleMap<EntityComponent> getComponents();
}
