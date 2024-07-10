package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.Entity;

import java.util.List;

/**
 * @author datafox
 */
public interface EntityDefinition {
    String getHandle();

    List<ComponentDefinition> getComponents();

    boolean isSingleton();

    Entity build(Engine engine);
}
