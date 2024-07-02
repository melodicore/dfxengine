package me.datafox.dfxengine.entities.definition.operation;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.DualParameterOperationDefinition;
import me.datafox.dfxengine.entities.api.definition.SingleParameterOperationDefinition;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.DualParameterOperation;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;

/**
 * @author datafox
 */
public class LerpOperationDefinition implements DualParameterOperationDefinition {
    @Override
    public DualParameterOperation build(Engine engine) {
        return Operations::lerp;
    }
}
