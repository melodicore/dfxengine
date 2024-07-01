package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.values.api.Modifier;

/**
 * @author datafox
 */
public interface ModifierDefinition {
    Modifier build(Engine engine);
}
