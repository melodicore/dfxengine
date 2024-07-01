package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.state.EngineState;
import me.datafox.dfxengine.handles.api.HandleMap;
import me.datafox.dfxengine.injector.api.Injector;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public interface Engine {
    Logger getLogger();

    Injector getInjector();

    HandleMap<Entity> getEntities();

    List<EntityLink> getLinks();

    Map<String,DataPack> getDataPacks();

    void registerPack(DataPack pack);

    void deregisterPack(DataPack pack, boolean removeDependents, boolean keepState);

    void loadPacks(boolean keepState);

    void setState(EngineState state);

    EngineState getState();

    void update(float delta);
}
