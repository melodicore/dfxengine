package me.datafox.dfxengine.entities.definition.operation;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.SingleParameterOperationDefinition;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

/**
 * @author datafox
 */
public class SubtractOperationDefinition implements SingleParameterOperationDefinition {
    @Override
    public SingleParameterOperation build(Engine engine) {
        return Operations::subtract;
    }
}
