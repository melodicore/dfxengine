package me.datafox.dfxengine.entities.api.definition;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

/**
 * @author datafox
 */
public interface SingleParameterOperationDefinition extends OperationDefinition {
    @Override
    SingleParameterOperation build(Engine engine);
}
