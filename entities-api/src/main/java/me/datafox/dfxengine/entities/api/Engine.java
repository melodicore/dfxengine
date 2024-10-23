package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;

import java.util.List;

/**
 * @author datafox
 */
public interface Engine {
    HandleMap<List<Entity>> getEntities();

    List<Entity> getEntities(String handle);

    List<Entity> getEntities(Handle handle);

    Entity getEntity(String handle);

    Entity getEntity(Handle handle);

    Entity createMultiEntity(String handle);

    Entity createMultiEntity(Handle handle);
}
