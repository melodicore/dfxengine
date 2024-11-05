package me.datafox.dfxengine.entities.api.definition;

import java.util.List;

/**
 * @author datafox
 */
public interface PackageDefinition {
    String getId();

    String getVersion();

    String getDeveloper();

    List<String> getDependencies();

    List<SpaceDefinition> getSpaces();

    List<EntityDefinition> getEntities();

    List<SystemDefinition> getSystems();
}
