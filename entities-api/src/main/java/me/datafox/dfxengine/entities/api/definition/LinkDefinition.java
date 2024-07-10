package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityLink;

/**
 * @author datafox
 */
public interface LinkDefinition {
    String getHandle();

    EntityLink build(Engine engine);
}
