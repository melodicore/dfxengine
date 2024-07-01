package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntitySystem;

/**
 * @author datafox
 */
public interface SystemDefinition {
    EntitySystem build(Engine engine);
}
