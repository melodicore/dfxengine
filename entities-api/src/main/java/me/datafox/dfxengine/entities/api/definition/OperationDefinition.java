package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.values.api.operation.Operation;

/**
 * @author datafox
 */
public interface OperationDefinition {
    int getParameterCount();

    Operation build(Engine engine);
}
