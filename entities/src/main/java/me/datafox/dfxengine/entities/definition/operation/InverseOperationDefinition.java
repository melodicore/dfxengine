package me.datafox.dfxengine.entities.definition.operation;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.SingleParameterOperationDefinition;
import me.datafox.dfxengine.entities.api.definition.SourceOperationDefinition;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.api.operation.SourceOperation;

/**
 * @author datafox
 */
public class InverseOperationDefinition implements SourceOperationDefinition {
    @Override
    public SourceOperation build(Engine engine) {
        return Operations::inverse;
    }
}
