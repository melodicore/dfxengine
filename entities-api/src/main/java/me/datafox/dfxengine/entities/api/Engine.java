package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.HandleMap;

/**
 * @author datafox
 */
public interface Engine {
    HandleMap<Entity> getEntities();
}
