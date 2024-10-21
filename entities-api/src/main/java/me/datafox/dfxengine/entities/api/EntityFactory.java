package me.datafox.dfxengine.entities.api;

import me.datafox.dfxengine.entities.api.definition.EntityDefinition;

/**
 * @author datafox
 */
public interface EntityFactory {
    Entity buildEntity(EntityDefinition definition);
}
