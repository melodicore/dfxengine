package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;

/**
 * @author datafox
 */
public interface DualParameterOperationDefinition extends OperationDefinition {
    @Override
    DualParameterOperation build(Engine engine);
}
