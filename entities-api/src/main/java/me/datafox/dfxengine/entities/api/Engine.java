package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.handles.api.Handle;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.Injector;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public interface Engine {
    Logger getLogger();

    Injector getInjector();

    HandleMap<List<Entity>> getEntities();

    void addEntityListener(EntityListener listener);

    void removeEntityListener(EntityListener listener);

    Entity createMultiEntity(Handle handle);

    List<Entity> createMultiEntities(List<Handle> handles);

    void removeMultiEntity(Entity entity);

    void removeMultiEntities(Collection<Entity> entities);

    Map<String,DataPack> getDataPacks();

    void addPack(DataPack pack);

    void removePack(String id, boolean removeDependents);

    void loadPacks(boolean keepState);

    Entity getCurrentEntity();

    EntityComponent getCurrentComponent();

    void setState(EngineState state);

    EngineState getState();

    void scheduleAction(EntityAction action, ActionParameters parameters);

    void update(float delta);
}
