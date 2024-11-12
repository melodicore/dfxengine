package me.datafox.dfxengine.entities.api.component;

import me.datafox.dfxengine.entities.api.definition.EntityDefinition;
import me.datafox.dfxengine.entities.api.definition.SystemDefinition;
import me.datafox.dfxengine.entities.api.entity.Entity;
import me.datafox.dfxengine.entities.api.entity.EntitySystem;

/**
 * @author datafox
 */
public interface EntityFactory {
    Entity buildEntity(EntityDefinition definition);

    EntitySystem buildSystem(SystemDefinition definition);
}
