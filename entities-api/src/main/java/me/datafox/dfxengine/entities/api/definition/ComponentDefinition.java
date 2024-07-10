package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;

import java.util.List;

/**
 * @author datafox
 */
public interface ComponentDefinition {
    String getHandle();

    List<DataDefinition> getData();

    List<LinkDefinition> getLinks();

    List<ActionDefinition> getActions();

    EntityComponent build(Engine engine);
}
