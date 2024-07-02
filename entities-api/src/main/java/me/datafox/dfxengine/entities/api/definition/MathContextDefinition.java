package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.values.api.operation.MapMathContext;

/**
 * @author datafox
 */
public interface MathContextDefinition {
    MapMathContext build(Engine engine);
}
