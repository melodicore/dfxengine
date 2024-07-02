package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityAction;

/**
 * @author datafox
 */
public interface ActionDefinition {
    String getHandle();

    EntityAction build(Engine engine);
}
