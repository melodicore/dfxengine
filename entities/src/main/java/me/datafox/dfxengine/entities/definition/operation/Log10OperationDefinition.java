package me.datafox.dfxengine.entities.definition.operation;

import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.definition.SourceOperationDefinition;
import me.datafox.dfxengine.math.utils.Operations;
import me.datafox.dfxengine.values.api.operation.SourceOperation;

/**
 * @author datafox
 */
public class Log10OperationDefinition implements SourceOperationDefinition {
    @Override
    public SourceOperation build(Engine engine) {
        return Operations::log10;
    }
}
