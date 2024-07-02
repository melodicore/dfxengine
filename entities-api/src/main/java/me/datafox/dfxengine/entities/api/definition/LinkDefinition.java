package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.link.EntityLink;

/**
 * @author datafox
 */
public interface LinkDefinition {
    EntityLink build(Engine engine);
}
