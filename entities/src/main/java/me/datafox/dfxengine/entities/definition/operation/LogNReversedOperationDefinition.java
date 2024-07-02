package me.datafox.dfxengine.entities.definition.operation;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.SingleParameterOperationDefinition;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.SingleParameterOperation;
import me.datafox.dfxengine.values.utils.Modifiers;

/**
 * @author datafox
 */
public class LogNReversedOperationDefinition implements SingleParameterOperationDefinition {
    @Override
    public SingleParameterOperation build(Engine engine) {
        return Modifiers.reverse(Operations::logN);
    }
}
