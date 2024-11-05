package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;

/**
 * @author datafox
 */
public interface EntityFactory {
    Entity buildEntity(EntityDefinition definition);

    EntitySystem buildSystem(SystemDefinition definition);
}
