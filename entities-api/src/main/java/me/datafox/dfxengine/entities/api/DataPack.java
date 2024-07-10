package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.SpaceDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;

import java.util.List;
import java.util.Set;

/**
 * @author datafox
 */
public interface DataPack {
    String getId();

    Set<String> getDependencies();

    String getDeveloper();

    List<SpaceDefinition> getSpaces();

    List<EntityDefinition> getEntities();

    List<SystemDefinition> getSystems();
}
