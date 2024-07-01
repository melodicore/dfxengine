package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityData;

/**
 * @author datafox
 */
public interface DataDefinition {
    String getHandle();

    String getTypeHandle();

    EntityData<?> build(Engine engine);
}
