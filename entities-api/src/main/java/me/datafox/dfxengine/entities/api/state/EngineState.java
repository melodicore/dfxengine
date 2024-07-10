package me.datafox.dfxengine.entities.api.state;

import java.util.List;
import java.util.Map;

/**
 * @author datafox
 */
public interface EngineState {
    List<EntityState> getSingleEntities();

    Map<String,List<EntityState>> getMultiEntities();
}
