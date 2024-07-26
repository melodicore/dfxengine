package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;

import java.util.List;

/**
 * @author datafox
 */
public interface DataPack {
    String getId();

    List<String> getDependencies();

    String getDeveloper();

    List<SpaceDefinition> getSpaces();

    List<EntityDefinition> getEntities();

    List<SystemDefinition> getSystems();
}
